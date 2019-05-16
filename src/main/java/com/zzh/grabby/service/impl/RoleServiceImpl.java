package com.zzh.grabby.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.entity.Role;
import com.zzh.grabby.mapper.RoleMapper;
import com.zzh.grabby.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzh
 * @since 2018-10-18
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;


    @Override
    public IPage<Role> selectRolePage(Page<Role> page, QueryWrapper<Role> wrapper) {
        return roleMapper.selectRolePage(page,wrapper);
    }
}
