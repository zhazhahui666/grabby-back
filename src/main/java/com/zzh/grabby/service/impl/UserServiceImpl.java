package com.zzh.grabby.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzh.grabby.common.constant.CommonConstant;
import com.zzh.grabby.entity.Permission;
import com.zzh.grabby.entity.Role;
import com.zzh.grabby.entity.User;
import com.zzh.grabby.entity.UserVo;
import com.zzh.grabby.mapper.PermissionMapper;
import com.zzh.grabby.mapper.RoleMapper;
import com.zzh.grabby.mapper.UserMapper;
import com.zzh.grabby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzh
 * @since 2018-10-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        wrapper.eq("status", CommonConstant.USER_STATUS_NORMAL);
        User user = userMapper.selectOne(wrapper);
        if (user != null) {
            //用户角色
            List<Role> roles = roleMapper.findByUid(user.getId());

            //用户对应的角色权限
            List<Permission> permissions = permissionMapper.findByUid(user.getId());

            user.setRoles(roles);
            user.setPermissions(permissions);

        }
        return user;

    }

    @Override
    public IPage<UserVo> selectPageByRole(Integer roleId, Page<UserVo> page) {
        return userMapper.selectPageByRole(page, roleId);
    }

    @Override
    public List<User> selectByRole(Integer roleId) {
        return userMapper.selectByRole(roleId);
    }

    @Override
    public IPage<UserVo> selectUserPage(Page<UserVo> page, User user) {
        return userMapper.selectUserPage(page, user);
    }
}
