package com.zzh.grabby.mapper;

import com.zzh.grabby.entity.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzh
 * @since 2018-12-01
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    Department findByNameAndPid(@Param("departmentName") String departmentName,@Param("pid")Integer pid);





}
