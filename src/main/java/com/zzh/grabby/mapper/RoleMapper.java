package com.zzh.grabby.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzh
 * @since 2018-10-18
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> findByUid(@Param("uid") Integer uid);


    IPage<Role> selectRolePage(Page<Role> page, @Param(Constants.WRAPPER)QueryWrapper<Role> wrapper);
}
