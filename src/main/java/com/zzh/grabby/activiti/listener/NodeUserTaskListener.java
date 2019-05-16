package com.zzh.grabby.activiti.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzh.grabby.activiti.entity.ActNode;
import com.zzh.grabby.activiti.service.ActNodeService;
import com.zzh.grabby.entity.User;
import com.zzh.grabby.exception.GrabbyException;
import com.zzh.grabby.service.UserRoleService;
import com.zzh.grabby.service.UserService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zzh
 * @date 2018/12/23
 */
@Component("nodeUserTaskListener")
public class NodeUserTaskListener implements TaskListener, Serializable {

    private static final long serialVersionUID = 2190559253653576032L;


    @Autowired
    private ActNodeService actNodeConfigService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity task = (TaskEntity) delegateTask;  //实际class
        String processInstanceId = task.getProcessInstanceId();//流程实例id
        String processDefinitionId = task.getProcessDefinitionId();//流程定义id
        String currentActName = task.getName(); //当前节点名称
        String actId = task.getTaskDefinitionKey();  //当前节点id

        //找到当前节点对应的处理人

        QueryWrapper<ActNode> wrapper = new QueryWrapper<>();
        wrapper.eq("process_define_id", processDefinitionId);
        wrapper.eq("act_id", actId);
        ActNode node = actNodeConfigService.getOne(wrapper);
        Integer handler = node.getHandler();
        switch (node.getHandlerType()) {
            case 1:  //处理人上级
                // TODO
                System.out.println("处理人上级TODO");
                break;
            case 2:  //角色
                //设置成候选人
                List<User> users = userService.selectByRole(handler);
                List<String> userIds = users.stream().map(user -> String.valueOf(user.getId())).collect(Collectors.toList());
                delegateTask.addCandidateUsers(userIds);
                break;
            case 3://指定人员
                delegateTask.setAssignee(String.valueOf(handler));
                break;
            default:
                throw new GrabbyException(currentActName + "节点没有设置办理人,请联系管理员设置");
        }
    }
}
