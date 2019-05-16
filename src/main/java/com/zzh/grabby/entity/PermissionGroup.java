package com.zzh.grabby.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzh.grabby.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zzh
 * @date 2019/1/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_permission_group")
public class PermissionGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    @TableField(exist = false)
    private List<Permission> permissionList = new ArrayList<>();

}
