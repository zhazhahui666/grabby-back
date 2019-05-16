package com.zzh.grabby.service;

import com.zzh.grabby.entity.Department;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzh
 * @since 2018-12-01
 */
public interface DepartmentService extends IService<Department> {

    List<Department> getTree();

    Department findByNameAndPid(String departmentName,Integer pid);
}
