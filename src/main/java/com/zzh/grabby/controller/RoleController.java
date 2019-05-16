package com.zzh.grabby.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.common.constant.CommonConstant;
import com.zzh.grabby.common.dto.PageDto;
import com.zzh.grabby.common.dto.ResultDto;
import com.zzh.grabby.entity.*;
import com.zzh.grabby.service.*;
import com.zzh.grabby.common.util.ResultUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzh
 * @since 2018-10-18
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;


    @GetMapping("/get_page")
    public ResultDto getPage(@ModelAttribute Role role, @ModelAttribute PageDto pageDto) {
        Page<Role> page = new Page<>(pageDto.getPageNumber(), pageDto.getPageSize());
        QueryWrapper<Role> wrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(role.getName())) {
            wrapper.lambda().like(Role::getName, role.getName());
        }
        wrapper.gt("id", CommonConstant.ROLE_SUPER_ADMIN_ID);
        wrapper.ge("id", CommonConstant.ROLE_SUPER_ADMIN_ID);

        wrapper.orderByDesc("create_time");
        IPage<Role> pageRole = roleService.selectRolePage(page, wrapper);
        return ResultUtil.setData(pageRole);
    }

    /**
     * 更新或者修改角色
     * @param role
     * @return
     */
    @PostMapping("/save-or-update")
    public ResultDto addRole(@ModelAttribute Role role){
        roleService.saveOrUpdate(role);
        return ResultUtil.setSuccessMsg("ok");
    }

    @GetMapping("/delete/{roleId}")
    public ResultDto deleteRole(@PathVariable("roleId") Integer roleId){
        //todo 判断是否工作流有引用
        Map<String, Object> map = new HashMap<>();
        map.put("role_id", roleId);

        //不能删除超级管理员
        //删除角色
        roleService.removeById(roleId);

        // 删除角色权限关系
        rolePermissionService.removeByMap(map);
        //todo删除角色人员关系
        userRoleService.removeByMap(map);
        return ResultUtil.setSuccess();
    }

    /**
     * 根据角色获取用户列表
     *
     * @param roleId
     * @return
     */
    @GetMapping("/get-user-list/{roleId}")
    public ResultDto getUserByRole(@PathVariable(value = "roleId") Integer roleId, @ModelAttribute PageDto pageDto) {
        Page<UserVo> page = new Page<>(pageDto.getPageNumber(), pageDto.getPageSize());
        IPage<UserVo> data = userService.selectPageByRole(roleId, page);
        return ResultUtil.setData(data);
    }

    /**
     * 根据角色获取权限树
     *
     * @param roleId
     * @return
     */
    @GetMapping("/get-permission-tree/{roleId}")
    public ResultDto getPermisisonByRole(@PathVariable(value = "roleId") Integer roleId) {

        List<PermissionGroup> groups =  permissionService.selectByRole(roleId);
        return ResultUtil.setData(groups);

    }





}
