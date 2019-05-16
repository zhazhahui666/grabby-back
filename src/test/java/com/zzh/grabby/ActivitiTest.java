package com.zzh.grabby;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


/**
 * @author zzh
 * @date 2018/12/21
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiTest {

    @Autowired
    private  IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private  HistoryService historyService;

//    public ProcessEngine getProcessEngine() {
//        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
//                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
//                .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/grabby?useUnicode=true&characterEncoding=utf-8&useSSL=false")
//                .setJdbcDriver("com.mysql.jdbc.Driver")
//                .setJdbcUsername("root")
//                .setJdbcPassword("chenliang110.")
//                .buildProcessEngine();
//
//        return processEngine;
//    }

//


    //部署流程
    @Test
    public void deploy() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("bpmn/hello.bpmn")
                .name("入门程序")
                .category("111111111111")
                .deploy();


    }

    //启动流程实例
    @Test
    public void startProcessInstance() {
        String processKey = "myProcess";  //流程图的唯一id
        String businessKey = "11111";  //业务表记录的id

        //设置流程发起人
        identityService.setAuthenticatedUserId("流程发起人");


        //启动流程
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processKey, businessKey);
        System.out.println("pid:" + pi.getId() + ",activitiId:" + pi.getActivityId() + ",pdId:" + pi.getProcessDefinitionId());

        //设置流程实例的名称
        runtimeService.setProcessInstanceName(pi.getId(), "流程实例的标题");
    }

    //获取所有节点
    @Test
    public void getAllNode() {
        //流程流程定义id获取模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel("myProcess:3:12504");

        List<Process> processes = bpmnModel.getProcesses();

        if (processes == null || processes.size() == 0) {
            System.out.println("没有流程在里面");
        }

//        for(Process process : processes){
//            Collection<FlowElement> elements = process.getFlowElements();
//            for(FlowElement element : elements){
//                ProcessNodeVo node = new ProcessNodeVo();
//                node.setId(element.getId());
//                node.setTitle(element.getName());
//                if(element instanceof StartEvent){
//                    // 开始节点
//                    node.setType(ActivitiConstant.NODE_TYPE_START);
//                }else if(element instanceof UserTask){
//                    // 用户任务
//                    node.setType(ActivitiConstant.NODE_TYPE_TASK);
//                    // 设置关联用户
//                    List<User> users = actNodeService.findUserByNodeId(element.getId());
//                    // 设置关联角色
//                    List<Role> roles = actNodeService.findRoleByNodeId(element.getId());
//                    node.setUsers(users);
//                    node.setRoles(roles);
//                }else if(element instanceof EndEvent){
//                    // 结束
//                    node.setType(ActivitiConstant.NODE_TYPE_END);
//                }else{
//                    // 排除其他连线或节点
//                    continue;
//                }
//                list.add(node);
//            }
//        }

    }

    //完成任务
    @Test
    public void completeTask() {
        String taskId = "42505";
        String userId = "zhang3";

        //判断是否在参与者里面
        List<IdentityLink> candidates = taskService.getIdentityLinksForTask(taskId);
        for (IdentityLink link : candidates) {
            System.out.println(link.getUserId());
            if (userId.equals(link.getUserId())) {
                System.out.println("在参与者中");
                taskService.setAssignee(taskId, userId);
                taskService.complete(taskId);
                return;
            }
        }
        System.out.println("不是该任务的参与者");
    }

    //获取待办任务
    @Test
    public void getTodo() {
        //流程实例查询
//        runtimeService.createProcessInstanceQuery()

        //代办理的任务查询
        List<Task> list = taskService.createTaskQuery().taskCandidateOrAssigned("zhang1").list();
        for (Task task : list) {
            System.out.println("办理人：" + task.getAssignee() + ",id:" + task.getId());
        }
    }

    //查看我的申请流程
    @Test
    public void getApply() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .startedBy("流程发起人")
                .list();
        for (HistoricProcessInstance ins : list) {
            System.out.println(ins.getProcessDefinitionName());
            System.out.println(ins.getName());
            System.out.println(ins.getBusinessKey());
            System.out.println(ins.getStartTime());
            System.out.println(ins.getEndTime());
            System.out.println(ins.getStartUserId());
            System.out.println("---------------------");
        }
    }


    //完成任务


    //查看我的任务


    //通过任务


}
