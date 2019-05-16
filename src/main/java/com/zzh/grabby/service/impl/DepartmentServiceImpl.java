package com.zzh.grabby.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.zzh.grabby.entity.Department;
import com.zzh.grabby.mapper.DepartmentMapper;
import com.zzh.grabby.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzh
 * @since 2018-12-01
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> getTree() {
        List<Department> departments = departmentMapper.selectList(null);

        Multimap<Integer, Department> depIdMap = ArrayListMultimap.create();

        for (Department department : departments) {
            depIdMap.put(department.getPid(), department);
        }

        List<Department> rootList = Lists.newArrayList();

        for (Department department : departments) {
            if (department.getPid() == null || department.getPid().equals(0)) {
                rootList.add(department);
            }
            //差排序
            List<Department> deps = (List<Department>) depIdMap.get(department.getId());
            department.setChildren(deps);
        }
        return rootList;
    }

    @Override
    public Department findByNameAndPid(String departmentName,Integer pid) {
        return departmentMapper.findByNameAndPid(departmentName,pid);
    }



}
