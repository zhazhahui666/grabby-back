package com.zzh.grabby.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzh.grabby.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzh
 * @since 2018-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_act_category")
@ToString
public class ActCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    @TableField(exist = false)
    private List<ActProcess> processDefinitionDtos = new ArrayList<>();


}
