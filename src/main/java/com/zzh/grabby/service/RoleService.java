package com.zzh.grabby.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzh
 * @since 2018-10-18
 */
public interface RoleService extends IService<Role> {


    IPage<Role> selectRolePage(Page<Role> page, QueryWrapper<Role> wrapper);
}
