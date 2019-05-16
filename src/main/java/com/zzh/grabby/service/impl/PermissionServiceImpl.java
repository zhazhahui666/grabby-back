package com.zzh.grabby.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.zzh.grabby.entity.Permission;
import com.zzh.grabby.entity.PermissionGroup;
import com.zzh.grabby.entity.RolePermission;
import com.zzh.grabby.mapper.PermissionGroupMapper;
import com.zzh.grabby.mapper.PermissionMapper;
import com.zzh.grabby.mapper.RolePermissionMapper;
import com.zzh.grabby.service.PermissionService;
import com.zzh.grabby.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzh
 * @since 2018-10-15
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private PermissionGroupMapper permissionGroupMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public List<PermissionGroup> selectByRole(Integer roleId) {
        List<Permission> list = permissionMapper.getByGroup();

        List<PermissionGroup> groups = permissionGroupMapper.selectList(null);

        List<Permission> checkedList = permissionMapper.selectByRole(roleId);

        Multimap<Integer, Permission> multimap = ArrayListMultimap.create();
        //分组
        for (Permission p : list) {
            for (Permission c : checkedList) {
                if(p.getId().equals(c.getId())){
                    p.setChecked(true);
                }
            }
            multimap.put(p.getGroupId(), p);
        }

        //拼装
        for (PermissionGroup group : groups) {
            List<Permission> permissions = (List<Permission>) multimap.get(group.getId());
            group.setPermissionList(permissions);
        }

        return groups;
    }

    @Override
    public List<PermissionGroup> getByGroup() {

        List<Permission> list = permissionMapper.getByGroup();
        List<PermissionGroup> groups = permissionGroupMapper.selectList(null);

        Multimap<Integer, Permission> multimap = ArrayListMultimap.create();
        //分组
        for (Permission p : list) {
            multimap.put(p.getGroupId(), p);
        }
        //拼装
        for (PermissionGroup group : groups) {
            List<Permission> permissions = (List<Permission>) multimap.get(group.getId());
            group.setPermissionList(permissions);
        }

        return groups;
    }

    @Transactional
    @Override
    public void saveRolePermision(Integer roleId, Integer[] permissionIds) {
        //删除old权限
        Map<String,Object> map = new HashMap<>();
        map.put("role_id", roleId);
        rolePermissionMapper.deleteByMap(map);

        //拼装
        List<RolePermission> rps = null;
        if(permissionIds != null && permissionIds.length>0){
             rps = Arrays.stream(permissionIds).map(item -> {
                RolePermission rp = new RolePermission();
                rp.setRoleId(roleId);
                rp.setPermissionId(item);
                return rp;
            }).collect(Collectors.toList());
        }

        //插入新权限
        if(rps != null){
            rolePermissionService.saveBatch(rps);
        }

    }
}
