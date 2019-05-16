package com.zzh.grabby.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2018-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_permission")
public class Permission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String title;

    private String pid;


    /**
     * 组件地址
     */
    private String component;

    /**
     * 1=菜单  2=按钮
     */
    private Integer type;

    /**
     * 排序
     */
    private BigDecimal sortOrder;

    /**
     * 匹配路径
     */
    private String path;


    /**
     * iframe地址
     */
    private String url;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单等级
     */
    private Integer level;

    /**
     * 分组id
     */
    private Integer groupId;

    @TableField(exist = false)
    private String groupName;


    @TableField(exist = false)
    private Boolean checked = false;


}
