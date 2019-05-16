package com.zzh.grabby.activiti.controller;


import com.zzh.grabby.activiti.entity.ActCategory;
import com.zzh.grabby.activiti.service.ActCategoryService;
import com.zzh.grabby.common.dto.ResultDto;
import com.zzh.grabby.common.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzh
 * @since 2018-12-23
 */
@RestController
@RequestMapping("/act-category")
public class ActCategoryController {

    @Autowired
    private ActCategoryService actCategoryService;


    /**
     * 获取分类列表
     *
     * @param processState 流程状态 1=正常 0=不正常
     * @return
     */
    @GetMapping("/get-process-category")
    public ResultDto getProcessCategoty(@RequestParam(required = false) Integer processState) {
        List<ActCategory> processCategoty = actCategoryService.getProcessCategoty(processState);
        return ResultUtil.setData(processCategoty);
    }

}
