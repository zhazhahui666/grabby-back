package com.zzh.grabby.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.common.dto.PageDto;
import com.zzh.grabby.common.dto.ResultDto;
import com.zzh.grabby.entity.Permission;
import com.zzh.grabby.entity.PermissionGroup;
import com.zzh.grabby.service.PermissionService;
import com.zzh.grabby.common.util.ResultUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzh
 * @since 2018-10-15
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/get_page")
    public ResultDto getPage(@ModelAttribute Permission permission,
                             @ModelAttribute PageDto pageDto) {
        Page<Permission> page = new Page<>(pageDto.getPageNumber(), pageDto.getPageSize());
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(permission.getName())) {
            wrapper.like("name", permission.getName());
        }
        IPage<Permission> pageUser = permissionService.page(page, wrapper);
        return ResultUtil.setData(pageUser);
    }

    @GetMapping("/get-permission-by-group")
    public ResultDto getByGroup() {
        List<PermissionGroup> groups = permissionService.getByGroup();
        return ResultUtil.setData(groups);
    }

    @PostMapping("/save-role-permissions/{roleId}")
    public ResultDto saveRolePermissions(@PathVariable(value = "roleId") Integer roleId,
                                         @RequestParam Integer[] permissionIds){

        permissionService.saveRolePermision(roleId,permissionIds);
        return ResultUtil.setSuccess();
    }

}
