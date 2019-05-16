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

import com.zzh.grabby.common.dto.ResultDto;
import com.zzh.grabby.common.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author Tijs Rademakers
 */
@RestController
@RequestMapping("/activiti-explorer/service")
@Slf4j
public class ModelEditorJsonRestResource implements ModelDataJsonConstants {


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(value = "/model/{modelId}/json", method = RequestMethod.GET, produces = "application/json")
    public ObjectNode getEditorJson(@PathVariable String modelId) {
        ObjectNode modelNode = null;
        Model model = repositoryService.getModel(modelId);

        if (model != null) {
            try {
                if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                    modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
                } else {
                    modelNode = objectMapper.createObjectNode();
                    modelNode.put(MODEL_NAME, model.getName());
                }
                modelNode.put(MODEL_ID, model.getId());
                ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(
                        new String(repositoryService.getModelEditorSource(model.getId()), "utf-8"));
                modelNode.put("model", editorJsonNode);

            } catch (Exception e) {
                log.error("Error creating model JSON", e);
                throw new ActivitiException("Error creating model JSON", e);
            }
        }
        return modelNode;
    }

//    /**
//     * 创建模型
//     */
//    @GetMapping("/create")
//    public ResultDto create() {
//        String modelName = "(空)";
//        String key = UUID.randomUUID().toString();
//        try {
//
//            // 初始模型
//            Model modelData = repositoryService.newModel();
//
//            ObjectNode modelObjectNode = objectMapper.createObjectNode();
//            modelData.setMetaInfo(modelObjectNode.toString());
//            modelData.setName(modelName);
//            modelData.setKey(key);
//
//            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, modelName);
//            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
//            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "这是描述");
//            //保存模型
//            repositoryService.saveModel(modelData);
//
//
//            //完善模型文件
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            //流程
//            ObjectNode editorNode = objectMapper.createObjectNode();
//            editorNode.put("id", "canvas");
//            editorNode.put("resourceId", "canvas");
//
//            //stencilset node
//            ObjectNode stencilSetNode = objectMapper.createObjectNode();
//            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
//
//            //属性node
//            ObjectNode properties = objectMapper.createObjectNode();
//            properties.put("process_id", key);
//            properties.put("name", "这是流程名字");
//
//            editorNode.replace("stencilset", stencilSetNode);
//            editorNode.replace("properties", properties);
//
//            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
//            return ResultUtil.setSuccess();
//        } catch (Exception e) {
//            log.error("创建模型失败：", e);
//            return ResultUtil.setErrorMsg("创建模型失败");
//        }
//    }


}
