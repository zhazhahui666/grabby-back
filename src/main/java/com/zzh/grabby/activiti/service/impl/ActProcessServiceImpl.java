package com.zzh.grabby.activiti.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zzh.grabby.activiti.entity.ActProcess;
import com.zzh.grabby.activiti.mapper.ActProcessMapper;
import com.zzh.grabby.activiti.service.ActProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzh.grabby.exception.GrabbyException;
import lombok.extern.slf4j.Slf4j;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzh
 * @since 2019-01-06
 */
@Service
@Slf4j
public class ActProcessServiceImpl extends ServiceImpl<ActProcessMapper, ActProcess> implements ActProcessService {


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ActProcessMapper actProcessMapper;

    @Override
    @Transactional
    public void addProcess(Integer categoryId, String processName) {
        String modelName = processName + "-模型";
        String key = UUID.randomUUID().toString().replaceAll("-", "");

        // 初始模型
        Model modelData = repositoryService.newModel();

        //模型基本信息
        ObjectNode modelObjectNode = objectMapper.createObjectNode();
        modelData.setName(modelName);
        modelData.setKey(key);
        modelData.setCategory(String.valueOf(categoryId));

        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, modelName);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processName + "的模型");
        modelData.setMetaInfo(modelObjectNode.toString());
        //保存模型
        repositoryService.saveModel(modelData);


        //完善模型文件
        ObjectMapper objectMapper = new ObjectMapper();

        //流程
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");

        //stencilset node
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
//        stencilSetNode.put("namespace", String.valueOf(categoryId));
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");

        //属性node
        ObjectNode properties = objectMapper.createObjectNode();
        properties.put("process_id", key);
        properties.put("name", processName);

        editorNode.replace("stencilset", stencilSetNode);
        editorNode.replace("properties", properties);

        try {
            //保存流程文件
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new GrabbyException("保存流程失败");
        }

        //插入自己的流程表t_act_process
        ActProcess actProcess = new ActProcess();
        actProcess.setCategoryId(categoryId);
        actProcess.setModelId(modelData.getId());
        actProcess.setName(processName);
        actProcess.setProcessKey(key);
        actProcess.setState(0);  //初始为草稿
        actProcess.setVersion(modelData.getVersion());

        actProcessMapper.insert(actProcess);
    }
}
