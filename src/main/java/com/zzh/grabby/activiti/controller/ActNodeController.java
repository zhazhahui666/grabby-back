package com.zzh.grabby.activiti.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.activiti.entity.ActNode;
import com.zzh.grabby.activiti.entity.ActProcess;
import com.zzh.grabby.activiti.service.ActNodeService;
import com.zzh.grabby.activiti.service.ActProcessService;
import com.zzh.grabby.common.dto.PageDto;
import com.zzh.grabby.common.dto.ResultDto;
import com.zzh.grabby.common.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzh
 * @since 2018-12-24
 */
@RestController
@RequestMapping("/act-node-config")
public class ActNodeController {


    @Autowired
    private ActNodeService actNodeService;

    @Autowired
    private ActProcessService actProcessService;

    /**
     * 设置节点处理人
     *
     * @param nodeConfigId
     * @param handlerType
     * @param handler
     * @return
     */
    @PostMapping("/update-node-handler")
    public ResultDto setNodeHander(@RequestParam Integer nodeConfigId,
                                   @RequestParam Integer handlerType,
                                   @RequestParam Integer handler,
                                   @RequestParam String handlerName) {
        ActNode nodeConfig = actNodeService.getById(nodeConfigId);

        nodeConfig.setHandlerType(handlerType);
        nodeConfig.setHandler(handler);
        nodeConfig.setHandlerName(handlerName);


//        actNodeConfigService.updateById(nodeConfig);
        actNodeService.saveOrUpdate(nodeConfig);

        return ResultUtil.setSuccess();
    }


    /**
     * 获取流程定义所有节点
     * @param processId
     * @param pageDto
     * @return
     */
    @GetMapping("/get-page/{processId}")
    public ResultDto getActs(@PathVariable String processId,
                             @ModelAttribute PageDto pageDto) {
        Page<ActNode> page = new Page<>(pageDto.getPageNumber(), pageDto.getPageSize());
        ActProcess process = actProcessService.getById(processId);
        String modelId = process.getModelId();
        QueryWrapper<ActNode> wrapper = new QueryWrapper<>();
        wrapper.eq("model_id", modelId);
        wrapper.isNull("process_define_id");
        IPage<ActNode> pageNode = actNodeService.page(page, wrapper);
        return ResultUtil.setData(pageNode);
    }


}
