package com.zzh.grabby.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzh.grabby.entity.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzh
 * @since 2018-10-16
 */
public interface UserMapper extends BaseMapper<User> {


    IPage<UserVo> selectPageByRole( Page<UserVo> page,@Param("roleId") Integer roleId);

    List<User> selectByRole(Integer roleId);

    IPage<UserVo> selectUserPage(Page<UserVo> page, @Param("user") User user);

}
