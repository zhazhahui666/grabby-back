package com.zzh.grabby.activiti.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzh.grabby.activiti.entity.ActCategory;
import com.zzh.grabby.activiti.entity.ActProcess;
import com.zzh.grabby.activiti.mapper.ActCategoryMapper;
import com.zzh.grabby.activiti.mapper.ActProcessMapper;
import com.zzh.grabby.activiti.service.ActCategoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzh
 * @since 2018-12-23
 */
@Service
public class ActCategoryServiceImpl extends ServiceImpl<ActCategoryMapper, ActCategory> implements ActCategoryService {

    @Autowired
    private ActCategoryMapper actCategoryMapper;


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private ActProcessMapper actProcessMapper;


    @Override
    public List<ActCategory> getProcessCategoty(Integer processState) {
//        //所有分类
//        List<ActCategory> actCategories = actCategoryMapper.selectList(null);
//
//        //所有流程定义
//        List<ProcessDefinition> list = repositoryService.createNativeProcessDefinitionQuery()
//                .sql("SELECT * FROM (SELECT * FROM "
//                        + managementService.getTableName(ProcessDefinition.class)
//                        + " arp  ORDER BY VERSION_ DESC) as t GROUP BY t.key_")
//                .list();
//
//        Map<Integer, ActCategory> parents = new HashMap<>();
//        //循环归类
//        for (ActCategory cat : actCategories) {
//            parents.put(cat.getId(), cat);
//        }
//
//        for (ProcessDefinition def : list) {
//            //可以一句话搞定，我只是懒得改
//            Integer category = Integer.valueOf(def.getCategory());
//            ActCategory actCategory = parents.get(category);
//            List<ActProcessDefinitionDto> processDefinitions = actCategory.getProcessDefinitionDtos();
//            ActProcessDefinitionDto dto = new ActProcessDefinitionDto();
//            processDefinitions.add(dto.adapt(def));
//        }
//        return actCategories;

        //所有分类
        List<ActCategory> actCategories = actCategoryMapper.selectList(null);

//        //所有流程定义
//        List<ProcessDefinition> list = repositoryService.createNativeProcessDefinitionQuery()
//                .sql("SELECT * FROM (SELECT * FROM "
//                        + managementService.getTableName(ProcessDefinition.class)
//                        + " arp  ORDER BY VERSION_ DESC) as t GROUP BY t.key_")
//                .list();

        QueryWrapper<ActProcess> wrapper = new QueryWrapper<>();
        if(processState != null){
            wrapper.eq("state", processState);
        }
//        wrapper.eq("state", processState);
        wrapper.orderByDesc("update_time");
        List<ActProcess> actProcesses = actProcessMapper.selectList(wrapper);

        Map<Integer, ActCategory> parents = new HashMap<>();
        //循环归类
        for (ActCategory cat : actCategories) {
            parents.put(cat.getId(), cat);
        }

        for (ActProcess process : actProcesses) {
            //可以一句话搞定，我只是懒得改
            ActCategory actCategory = parents.get(process.getCategoryId());
            List<ActProcess> processDefinitions = actCategory.getProcessDefinitionDtos();
            processDefinitions.add(process);
        }
        return actCategories;
    }
}
