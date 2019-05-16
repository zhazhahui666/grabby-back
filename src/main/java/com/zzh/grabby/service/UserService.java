package com.zzh.grabby.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzh.grabby.entity.UserVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzh
 * @since 2018-10-16
 */
public interface UserService extends IService<User> {

    //TODO  加缓存
    User findByUsername(String username);

    IPage<UserVo> selectPageByRole(Integer roleId, Page<UserVo> page);

    List<User> selectByRole(Integer roleId);

    IPage<UserVo> selectUserPage(Page<UserVo> page, User user);
}
