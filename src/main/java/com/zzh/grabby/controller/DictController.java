package com.zzh.grabby.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.common.dto.PageDto;
import com.zzh.grabby.common.dto.ResultDto;
import com.zzh.grabby.common.util.ResultUtil;
import com.zzh.grabby.entity.Dict;
import com.zzh.grabby.entity.DictOption;
import com.zzh.grabby.exception.GrabbyException;
import com.zzh.grabby.service.DictOptionService;
import com.zzh.grabby.service.DictService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zzh
 * @since 2019-01-23
 */
@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @Autowired
    private DictOptionService dictOptionService;

    /**
     * 新增或者更新字典
     * @param dictVo
     * @return
     */
    @PostMapping("/save-or-update-dict")
    public ResultDto saveOrUpdate(@Valid Dict dictVo){
        Dict dict = null;
        if (dictVo.getId()!= null) { //更新
            dict = dictService.getById(dictVo.getId());
            if(dict == null){
                throw new GrabbyException("字典不存在");
            }
            dict.setName(dictVo.getName());
            dict.setDescription(dictVo.getDescription());
            dict.setRank(dictVo.getRank());
            dict.setDictCode(dictVo.getDictCode());
        }else{ //新增
            dict = dictVo;
        }
        dictService.saveOrUpdate(dict);
        return ResultUtil.setSuccess();
    }

    /**
     * 查询字典分页
     */
    @GetMapping("/get-page")
    public ResultDto getPage(@RequestParam String dictName,
                             @ModelAttribute PageDto pageDto){
        Page<Dict> page = new Page<>(pageDto.getPageNumber(), pageDto.getPageSize());
        QueryWrapper<Dict> wrapper = wrapper =  new QueryWrapper<>();
        if(!StringUtils.isEmpty(dictName)){
            wrapper.like("name", dictName);
        }
        wrapper.orderByAsc("rank");
        IPage<Dict> pageData = dictService.page(page, wrapper);
        return ResultUtil.setData(pageData);
    }

    /**
     * 根据字典type获取选项
     * @param dictCode
     * @return
     */
    @GetMapping("/get-option-by-code")
    public ResultDto getOptionByCode(@RequestParam String dictCode){
        List<DictOption> list =  dictService.getOptionByCode(dictCode);
        return ResultUtil.setData(list);
    }


    /**
     * 新增或者更新选项
     * @param optionParam
     * @return
     */
    @PostMapping("/save-or-update-option")
    public ResultDto saveOrUpdateOption(@Valid DictOption optionParam){
        DictOption option = null;
        if (optionParam.getId()!= null) { //更新
            option = dictOptionService.getById(optionParam.getId());
            if(option == null){
                throw new GrabbyException("字典选项不存在");
            }
            option.setName(optionParam.getName());
            option.setRank(optionParam.getRank());
            option.setValue(optionParam.getValue());
        }else{ //新增
            option = optionParam;
        }
        dictOptionService.saveOrUpdate(option);
        return ResultUtil.setSuccess();
    }

    /**
     * 删除选项
     * @param optionId
     * @return
     */
    @DeleteMapping("/delete-option/{optionId}")
    public ResultDto deleteOption(@PathVariable Integer optionId){
        boolean res = dictOptionService.removeById(optionId);
        if(res){
            return ResultUtil.setSuccess();
        }else{
            return ResultUtil.setErrorMsg("删除失败");
        }
    }




}
