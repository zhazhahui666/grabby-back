/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zzh.grabby.activiti.modeler.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zzh.grabby.activiti.entity.ActNode;
import com.zzh.grabby.activiti.service.ActNodeService;
import com.zzh.grabby.activiti.service.ActProcessService;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Tijs Rademakers
 */
@RestController
@RequestMapping("/activiti-explorer/service")
public class ModelSaveRestResource implements ModelDataJsonConstants {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ModelSaveRestResource.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActNodeService actNodeService;

    @Autowired
    private ActProcessService actProcessService;



    /**
     * 模型保存
     *
     * @param modelId
     * @param json_xml
     * @param svg_xml
     * @param name
     * @param description
     */
    @RequestMapping(value = "/model/{modelId}/save", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void saveModel(@PathVariable String modelId, @RequestParam String json_xml,
                          @RequestParam String svg_xml, @RequestParam String name, @RequestParam String description) {
        try {
            //保存model基本信息
            ObjectMapper objectMapper = new ObjectMapper();
            Model model = repositoryService.getModel(modelId);
            ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            modelJson.put(MODEL_NAME, name);
            modelJson.put(MODEL_DESCRIPTION, description);
            model.setMetaInfo(modelJson.toString());
            model.setName(name);
            model.setVersion(model.getVersion() + 1); //model版本升级
            repositoryService.saveModel(model);

            //流程json转对象
            ObjectNode objectNode = (ObjectNode) objectMapper.readTree(json_xml);
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(objectNode);

            //用户提交的任务和创建，结束节点信息
            List<ActNode> commitNodes = new ArrayList<>();

            //配置流程
            List<Process> processes = bpmnModel.getProcesses();
            if (processes == null || processes.size() == 0) {
                System.out.println("没有流程在里面");
            }
            for (Process process : processes) {
                Collection<FlowElement> elements = process.getFlowElements();
                for (FlowElement element : elements) {

                    ActNode actNode = new ActNode();
                    actNode.setActId(element.getId());
                    actNode.setActName(element.getName());
                    actNode.setModelId(modelId);

                    if (element instanceof StartEvent) {
                        // todo 开始节点
                        commitNodes.add(actNode.setActType("startEvent"));
                    } else if (element instanceof UserTask) {
                        commitNodes.add(actNode.setActType("userTask"));
                        UserTask userTask = (UserTask) element;
                        // 用户任务
                        List<ActivitiListener> taskListeners = new ArrayList<ActivitiListener>();
                        ActivitiListener listener = new ActivitiListener();
                        listener.setId("");
                        listener.setEvent("create");
                        listener.setImplementationType("delegateExpression");
                        listener.setImplementation("${nodeUserTaskListener}");
                        taskListeners.add(listener);
                        //添加用户任务监听
                        userTask.setTaskListeners(taskListeners);
                    } else if (element instanceof EndEvent) {
                        // todo 结束节点
                        commitNodes.add(actNode.setActType("endEvent"));
                    } else if (element instanceof SequenceFlow) {
                        //连线，暂时不处理
                    } else {
                        // 其他节点，可能是网关啥的
                        continue;
                    }
                }
            }

            //保存流程配置
            ObjectNode bpmnJson = new BpmnJsonConverter().convertToJson(bpmnModel);
            repositoryService.addModelEditorSource(model.getId(), bpmnJson.toString().getBytes("utf-8"));

            //保存图片信息
            InputStream svgStream = new ByteArrayInputStream(svg_xml.getBytes("utf-8"));
            TranscoderInput input = new TranscoderInput(svgStream);

            PNGTranscoder transcoder = new PNGTranscoder();
            // Setup output
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);

            // Do the transformation
            transcoder.transcode(input, output);
            final byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();


            //查出模型对应的节点信息
            QueryWrapper<ActNode> wrapper = new QueryWrapper<>();
            wrapper.eq("model_id", modelId);
            wrapper.isNull("process_define_id");
            List<ActNode> actNodes = actNodeService.list(wrapper);
            //没有节点信息
            if (actNodes == null || actNodes.size() == 0) {
                //直接插入
                actNodeService.saveBatch(commitNodes);
            } else {  //有节点，可能有处理人 todo
                //原来的删除
                actNodeService.remove(wrapper);
                //分离出用户提交有的，数据库中没有的进行添加
                for (ActNode commitNode : commitNodes) {
                    for (ActNode actNode : actNodes) {
                        if (actNode.getActId().equals(commitNode.getActId())) {
                            commitNode.setHandler(actNode.getHandler());
                            commitNode.setHandlerName(actNode.getHandlerName());
                            commitNode.setHandlerType(actNode.getHandlerType());
                            break;
                        }
                    }
                }
                actNodeService.saveOrUpdateBatch(commitNodes);
            }

//      //部署流程
//      Model modelData = repositoryService.getModel(modelId);
//      ObjectNode modelNode = (ObjectNode) objectMapper.readTree(repositoryService.getModelEditorSource(modelData.getId()));
//      byte[] bpmnBytes = null;
//      BpmnModel model2 = new BpmnJsonConverter().convertToBpmnModel(modelNode);
//      bpmnBytes = new BpmnXMLConverter().convertToXML(model2);
//
//      String processName = modelData.getName() + ".bpmn";
//
//      //部署流程
//      Deployment deployment = repositoryService.createDeployment()
//              .name(modelData.getName())
//              .addString(processName, StringUtils.toEncodedString(bpmnBytes, Charset.forName("UTF-8")))
//              .deploy();

        } catch (Exception e) {
            LOGGER.error("Error saving model", e);
            throw new ActivitiException("Error saving model", e);
        }
    }


    /**
     * 流程图元素转换成actiNode
     *
     * @param element
     * @return
     */
    public ActNode transfer2ActNode(FlowElement element, String modelId) {
        ActNode actNode = new ActNode();
        actNode.setActId(element.getId());
        actNode.setActName(element.getName());
        actNode.setModelId(modelId);
        if (element instanceof UserTask) {
            actNode.setActType("userTask");
        } else if (element instanceof StartEvent) {
            actNode.setActType("startEvent");
        } else if (element instanceof EndEvent) {
            actNode.setActType("endEvent");
        } else {
            return null;
        }
        return actNode;
    }


}
