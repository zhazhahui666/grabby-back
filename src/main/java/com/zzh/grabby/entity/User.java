package com.zzh.grabby.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzh.grabby.common.base.BaseEntity;
import com.zzh.grabby.common.constant.CommonConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzh
 * @since 2018-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_user")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String nickname;

    private String realname;

    private String mobile;

    private String email;

    private String address;

    private Integer sex;

    private String avatar;


    /**
     * 部门id
     */
    private String departmentId;

    /**
     * 1=普通用户 2=管理员
     */
    private Integer type;

    /**
     * 1=正常 2=锁定
     */
    private Integer status = CommonConstant.USER_STATUS_NORMAL;


    @TableField(exist=false)
    private List<Permission> permissions;


    @TableField(exist=false)
    private List<Role> roles;






}
