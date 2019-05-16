package com.zzh.grabby.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzh.grabby.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzh
 * @since 2018-12-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_act_node")
public class ActNode extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 流程定义id
     */
    private String processDefineId;

    /**
     * 节点id
     */
    private String actId;

    /**
     * 节点名称
     */
    private String actName;


    /**
     * 节点类型
     */

    private String actType;

    /**
     * 模型id
     */
    private String modelId;

    /**
     * 处理人类型   1=创建人直接上级  2=指定角色 3=指定人员
     */
    private Integer handlerType;

    /**
     * 处理人
     */
    private Integer handler;


    /**
     * 处理人名称
     */
    private String handlerName;


}
