package com.zzh.grabby.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzh.grabby.common.base.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzh
 * @since 2019-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_act_process")
//@Builder //会导致所有方法私有化
public class ActProcess extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 使用状态
     */
    public static Integer STATE_RELEASE = 1;

    /**
     * 暂停状态
     */
    public static Integer STATE_STOP = 0;

    /**
     * 流程定义id
     */
    private String processDefineId;

    /**
     * 流程唯一key
     */
    private String processKey;

    private String modelId;

    /**
     * 名称
     */
    private String name;

    private Integer categoryId;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 0=未启用  1=启用
     */
    private Integer state;


}
