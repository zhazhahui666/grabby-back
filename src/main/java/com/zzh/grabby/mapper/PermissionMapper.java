package com.zzh.grabby.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zzh
 * @since 2018-10-15
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> findByUid(@Param("uid") Integer uid);

    List<Permission> selectByRole(@Param("roleId")Integer roleId);

    List<Permission> getByGroup();
}
