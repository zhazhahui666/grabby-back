package com.zzh.grabby.activiti.dto;

import lombok.Data;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zzh
 * @date 2018/12/25
 */
@Data
public class ProcessDto implements Serializable {

    private static final long serialVersionUID = -1928352968469274632L;

    /**
     * 流程实例id
     */
    private String processInsId;

    /**
     * 请求标题
     */
    private String title;

    /**
     * 业务记录表id,记录在流程定义中
     */
    private Integer tableId;

    /**
     * 业务记录id
     */
    private Integer businessId;

    /**
     * 当前节点名称
     */
    private String currentActName;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 正在进行的任务id
     */
    private String taskId;

    /**
     * 任务结束时间
     */
    private Date taskEndTime;

    /**
     * 流程结束时间
     */
    private Date endTime;

    private ProcessDto adapt(HistoricProcessInstance instance) {
        this.processInsId = instance.getId();
        this.title = instance.getName();
        this.businessId = Integer.valueOf(instance.getBusinessKey());
        this.createTime = instance.getStartTime();
        this.createUser = Integer.valueOf(instance.getStartUserId());
        this.endTime = instance.getEndTime();
        return this;
    }


    public ProcessDto adapt(HistoricProcessInstance instance, Task task) {
        this.adapt(instance);
        this.taskId = task == null ? null : task.getId();
        this.currentActName = task == null ? "归档" : task.getName();
        return this;
    }

    public ProcessDto adapt(HistoricProcessInstance instance, HistoricTaskInstance task) {
        this.adapt(instance);
        this.taskId = task == null ? null : task.getId();
        this.currentActName = task == null ? "归档" : task.getName();
        this.taskEndTime = task.getEndTime();
        return this;
    }


}
