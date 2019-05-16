package com.zzh.grabby.controller;


import com.zzh.grabby.common.dto.ResultDto;
import com.zzh.grabby.entity.Department;
import com.zzh.grabby.service.DepartmentService;
import com.zzh.grabby.common.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zzh
 * @since 2018-12-01
 */
@Api(description = "部门管理")
@RestController
@RequestMapping("/department")
@Validated
public class DepartmentController {


    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/get_tree")
    @ApiOperation(value = "获取部门树")
    public ResultDto getTree(){
        List<Department> departments = departmentService.getTree();
        return ResultUtil.setData(departments);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增部门")
    public  ResultDto add(@NotBlank(message = "部门名称不能为空") @RequestParam String name,
                          @RequestParam Integer pid,
                          @RequestParam String description,
                          @RequestParam Double rank){
        Department department = new Department();
        department.setName(name);
        department.setPid(pid);
        department.setDescription(description);
        department.setRank(rank);
        departmentService.save(department);
        return ResultUtil.setSuccess();
    }


}
