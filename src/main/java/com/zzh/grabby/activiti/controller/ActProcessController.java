package com.zzh.grabby.activiti.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zzh.grabby.activiti.dto.ProcessDto;
import com.zzh.grabby.activiti.entity.ActNode;
import com.zzh.grabby.activiti.entity.ActProcess;
import com.zzh.grabby.activiti.service.ActNodeService;
import com.zzh.grabby.activiti.service.ActProcessService;
import com.zzh.grabby.common.dto.PageDto;
import com.zzh.grabby.common.dto.ResultDto;
import com.zzh.grabby.common.util.ResultUtil;
import com.zzh.grabby.common.util.SysContext;
import com.zzh.grabby.common.vo.PageVo;
import com.zzh.grabby.config.security.SecurityUser;
import com.zzh.grabby.exception.GrabbyException;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zzh
 * @date 2018/12/23
 */

@RestController
@RequestMapping("/act-process")
@Slf4j
public class ActProcessController {


    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    @Autowired
    private ActProcessService actProcessService;

    @Autowired
    private ActNodeService actNodeService;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 启动流程
     *
     * @param processKey
     * @return
     */
    @GetMapping("/start-process/{processKey}")
    public ResultDto startProcess(@PathVariable String processKey) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser user = (SecurityUser) authentication.getPrincipal();

        String businessKey = "11111";  //todo 业务表记录的id,暂时写死，

        //设置流程发起人
        identityService.setAuthenticatedUserId(String.valueOf(user.getId()));

        //启动流程
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processKey, businessKey);
        System.out.println("pid:" + pi.getId() + ",activitiId:" + pi.getActivityId() + ",pdId:" + pi.getProcessDefinitionId());


        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .orderByProcessDefinitionVersion()
                .desc()
                .listPage(0, 1);
        ProcessDefinition processDefinition = null;
        if (processDefinitions != null && processDefinitions.size() > 0) {
            processDefinition = processDefinitions.get(0);
        } else {
            ResultUtil.setErrorMsg("流程定义不存在");
        }
        //设置流程实例的名称  todo 名字为自己t_act_process的名字
        runtimeService.setProcessInstanceName(pi.getId(), processDefinition.getName() + "-" + user.getUsername());
        return ResultUtil.setSuccess();
    }


    /**
     * 获取待办任务
     *
     * @return
     */
    @GetMapping("/todo-list")
    public ResultDto getTodoList(@ModelAttribute PageDto pageDto) {
        SecurityUser user = SysContext.getUser();
        //我的待办任务
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateOrAssigned(String.valueOf(user.getId()))
                .orderByTaskCreateTime()
                .desc()
                .listPage(pageDto.getPageNumber() - 1, pageDto.getPageSize());

        long count = taskService.createTaskQuery()
                .taskCandidateOrAssigned(String.valueOf(user.getId()))
                .count();

        List<ProcessDto> list = new ArrayList<>();

        if (tasks != null & tasks.size() > 0) {
            //流程实例id集合
            Set<String> processInsIds = tasks.stream()
                    .map(task -> task.getProcessInstanceId())
                    .collect(Collectors.toSet());

            //任务hash
            Map<String, Task> taskMaps = new HashMap<>();
            for (Task task : tasks) {
                taskMaps.put(task.getProcessInstanceId(), task);
            }

            //流程实例集合
            List<HistoricProcessInstance> processInsList = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceIds(processInsIds)
                    .orderByProcessInstanceStartTime()
                    .desc()
                    .list();

            //拼装页面需要的数据
            list = processInsList.stream()
                    .map(instance -> {
                        ProcessDto processDto = new ProcessDto();
                        processDto.adapt(instance, taskMaps.get(instance.getId()));
                        return processDto;
                    }).collect(Collectors.toList());

        }
        PageVo<ProcessDto> pageVo = new PageVo<>();
        pageVo.setRecords(list);
        pageVo.setTotal(count);
        return ResultUtil.setData(pageVo);
    }


    /**
     * 获取我的申请
     *
     * @param pageDto
     * @return
     */
    @GetMapping("/get_my_apply")
    public ResultDto getMyApply(@ModelAttribute PageDto pageDto) {
        SecurityUser user = SysContext.getUser();
        //流程实例集合
        List<HistoricProcessInstance> processInsList = historyService.createHistoricProcessInstanceQuery()
                .startedBy(String.valueOf(user.getId()))
                .orderByProcessInstanceStartTime()
                .desc()
                .listPage(pageDto.getPageNumber() - 1, pageDto.getPageSize());

        long count = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(String.valueOf(user.getId()))
                .count();

        List<String> processInsIds = processInsList.stream().map(ins -> ins.getId()).collect(Collectors.toList());

        List<ProcessDto> list = new ArrayList<>();

        if (processInsIds != null && processInsIds.size() > 0) {
            //流程实例有关的任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceIdIn(processInsIds)
                    .list();

            if (tasks != null & tasks.size() > 0) {
                //任务hash,流程实例可能对应多个任务，hash会覆盖
                Map<String, Task> taskMaps = new HashMap<>();
                for (Task task : tasks) {
                    taskMaps.put(task.getProcessInstanceId(), task);
                }
                //拼装页面需要的数据
                list = processInsList.stream()
                        .map(instance -> {
                            ProcessDto processDto = new ProcessDto();
                            processDto.adapt(instance, taskMaps.get(instance.getId()));
                            return processDto;
                        }).collect(Collectors.toList());
            }

        }

        PageVo<ProcessDto> pageVo = new PageVo<>();
        pageVo.setRecords(list);
        pageVo.setTotal(count);
        return ResultUtil.setData(pageVo);
    }

    /**
     * 已办任务
     *
     * @param pageDto
     * @return
     */
    @GetMapping("/done-list")
    public ResultDto getDoneList(@ModelAttribute PageDto pageDto) {

        SecurityUser user = SysContext.getUser();

        //已办任务
        List<HistoricTaskInstance> hisTasks = historyService.createHistoricTaskInstanceQuery()
//                .taskCandidateUser(String.valueOf(user.getId()))
                .taskAssignee(String.valueOf(user.getId()))
                .finished()
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .listPage(pageDto.getPageNumber() - 1, pageDto.getPageSize());

        long count = historyService.createHistoricTaskInstanceQuery()
                .taskCandidateUser(String.valueOf(user.getId()))
                .count();

        //待返回的集合
        List<ProcessDto> list = new ArrayList<>();

        if (hisTasks != null & hisTasks.size() > 0) {
            //流程实例id集合
            Set<String> processInsIds = hisTasks.stream()
                    .map(task -> task.getProcessInstanceId())
                    .collect(Collectors.toSet());

            //任务hash
            Map<String, HistoricTaskInstance> taskMaps = new HashMap<>();
            for (HistoricTaskInstance task : hisTasks) {
                taskMaps.put(task.getProcessInstanceId(), task);
            }

            //流程实例集合
            List<HistoricProcessInstance> processInsList = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceIds(processInsIds)
                    .orderByProcessInstanceStartTime()
                    .desc()
                    .list();

            //拼装页面需要的数据
            list = processInsList.stream()
                    .map(instance -> {
                        ProcessDto processDto = new ProcessDto();
                        processDto.adapt(instance, taskMaps.get(instance.getId()));
                        return processDto;
                    }).collect(Collectors.toList());

        }
        PageVo<ProcessDto> pageVo = new PageVo<>();
        pageVo.setRecords(list);
        pageVo.setTotal(count);
        return ResultUtil.setData(pageVo);

    }

    /**
     * 通过
     *
     * @param taskId
     * @return
     */
    @PostMapping("/pass")
    public ResultDto pass(@RequestParam String taskId) {
        SecurityUser user = SysContext.getUser();
        String userId = String.valueOf(user.getId());

        //todo 前台传过来
        String comment = "这是评论";

        //判断是否在参与者里面
        List<IdentityLink> candidates = taskService.getIdentityLinksForTask(taskId);
        for (IdentityLink link : candidates) {
            System.out.println(link.getUserId());
            if (userId.equals(link.getUserId())) {
                System.out.println("在参与者中");
                taskService.setAssignee(taskId, userId);
                taskService.addComment(taskId, null, comment);
                taskService.complete(taskId);
                return ResultUtil.setSuccess();
            }
        }
        return ResultUtil.setErrorMsg("不是任务的办理人");
    }


    /**
     * 获取模型列表
     *
     * @param pageDto
     * @return
     */
    @GetMapping("/get-model-page")
    public ResultDto getModelList(@ModelAttribute PageDto pageDto) {
        List<Model> models = repositoryService.createModelQuery()
                .orderByLastUpdateTime()
                .desc()
                .listPage(pageDto.getPageNumber() - 1, pageDto.getPageSize());
        long count = repositoryService.createModelQuery().count();

        PageVo<Model> pageVo = new PageVo<>();
        pageVo.setTotal(count);
        pageVo.setRecords(models);
        return ResultUtil.setData(pageVo);

    }

    /**
     * 新增流程
     *
     * @param categoryId  分类id
     * @param processName 流程名称
     * @return
     */
    @PostMapping("/add")
    public ResultDto add(@RequestParam Integer categoryId,
                         @RequestParam String processName) {

        actProcessService.addProcess(categoryId, processName);
        return ResultUtil.setSuccess();

    }

    /**
     * 发布流程
     */
    @GetMapping("/deploy/{processId}")
    public ResultDto deploy(@PathVariable Integer processId) throws IOException {
        ActProcess process = actProcessService.getById(processId);

        Model modelData = repositoryService.getModel(process.getModelId());
        ObjectNode modelNode = (ObjectNode) objectMapper.readTree(repositoryService.getModelEditorSource(modelData.getId()));
        BpmnModel model2 = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model2);

        String processName = modelData.getName() + ".bpmn";

        //部署流程
        Deployment deployment = repositoryService.createDeployment()
                .name(modelData.getName())
                .addString(processName, StringUtils.toEncodedString(bpmnBytes, Charset.forName("UTF-8")))
                .deploy();


        //得到流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(process.getProcessKey())
                .orderByProcessDefinitionVersion()
                .desc()
                .list()
                .get(0);
        String processDefineId = processDefinition.getId();

        //插入节点信息
        QueryWrapper<ActNode> wrapper = new QueryWrapper<>();
        wrapper.eq("model_id", process.getModelId());
        wrapper.isNull("process_define_id");
        List<ActNode> list = actNodeService.list(wrapper);

        List<ActNode> saveList = new ArrayList<>();
        for (ActNode node : list) {
            ActNode actNode = new ActNode();
            BeanUtils.copyProperties(node, actNode);
            actNode.setId(null);
            actNode.setProcessDefineId(processDefineId);
            saveList.add(actNode);
        }
        actNodeService.saveBatch(saveList);

        //流程状态改为正常
        process.setState(ActProcess.STATE_RELEASE);
        actProcessService.updateById(process);
        return ResultUtil.setSuccess();
    }

    /**
     * 删除流程
     *
     * @param processId
     * @return
     */
    @GetMapping("/delete/{processId}")
    public ResultDto deleteById(@PathVariable Integer processId) {
        //判断是否有运行的记录

        //TODO 暂时先删了t_act_process的记录
        actProcessService.removeById(processId);
        return ResultUtil.setSuccess();
    }

    /**
     * 流程实例id
     *
     * @param processInsId
     */
    @GetMapping("/get-process-image/{processInsId}")
    public void getProcessImage(@PathVariable String processInsId, HttpServletResponse response) {
        log.info("[开始]-获取流程图图像");
        // 设置页面不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            //  获取历史流程实例
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInsId).singleResult();


            // 获取流程定义
            ProcessDefinition processDefinition = repositoryService
                    .createProcessDefinitionQuery()
                    .processDefinitionId(historicProcessInstance.getProcessDefinitionId())
                    .singleResult();

            // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
            List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInsId).orderByHistoricActivityInstanceId().asc().list();

            // 已执行的节点ID集合
            List<String> executedActivityIdList = new ArrayList<String>();
            @SuppressWarnings("unused")
            int index = 1;
            log.info("获取已经执行的节点ID");
            for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
                executedActivityIdList.add(activityInstance.getActivityId());
                log.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + " : " + activityInstance.getActivityName());
                index++;
            }
            //模型对象
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());

            // 获取流程图图像字符流
            ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
//            InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList);
            //获得流程图的流对象，这里显示的加载了字体
            InputStream imageStream = diagramGenerator.generateDiagram(
                    bpmnModel,
                    "png",
                    executedActivityIdList, Collections.<String>emptyList(),
                    "宋体",
                    "宋体",
                    "宋体",
                    null,
                    1.0);

//            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setContentType("image/png");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(processDefinition.getName() + ".png", "UTF-8"));

            OutputStream os = response.getOutputStream();
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            imageStream.close();
            log.info("[完成]-获取流程图图像");
        } catch (Exception e) {
            log.error("【异常】-获取流程图失败！" + e.getMessage());
            throw new GrabbyException("获取流程图失败");
        }
    }


}
