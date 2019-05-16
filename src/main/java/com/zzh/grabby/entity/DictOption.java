package com.zzh.grabby.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzh.grabby.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzh
 * @since 2019-01-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_dict_option")
public class DictOption extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 字典id
     */
    private Integer dictId;

    /**
     * 选项名称
     */
    @NotEmpty(message =  "选项名称不能为空")
    private String name;

    /**
     * 字典名称
     */
    @TableField(exist = false)
    private String dictName;

    /**
     * 自定义选项值
     */
    @NotEmpty(message = "选项值不能为空")
    private String value;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    private Double rank;


}
