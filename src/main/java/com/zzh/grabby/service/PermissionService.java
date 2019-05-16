package com.zzh.grabby.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzh.grabby.entity.PermissionGroup;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzh
 * @since 2018-10-15
 */
public interface PermissionService extends IService<Permission> {

    List<PermissionGroup> selectByRole(Integer roleId);

    List<PermissionGroup> getByGroup();

    void saveRolePermision(Integer roleId, Integer[] permissionIds);
}
