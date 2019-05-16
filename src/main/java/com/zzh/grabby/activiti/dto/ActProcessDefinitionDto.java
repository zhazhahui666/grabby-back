//package com.zzh.grabby.activiti.dto;
//
//import lombok.Data;
//import org.activiti.engine.repository.ProcessDefinition;
//import org.springframework.beans.BeanUtils;
//
///**
// * @author zzh
// * @date 2018/12/24
// */
//@Data
//public class ActProcessDefinitionDto {
//
//    private String id;
//
//    private String category;
//
//    private String name;
//
//    private String key;
//
//    private String description;
//
//    private String version;
//
//    private String resourceName;
//
//    private String deploymentId;
//
//
//    private String diagramResourceName;
//
//    private String tenantId;
//
//    public ActProcessDefinitionDto adapt(ProcessDefinition def) {
//        BeanUtils.copyProperties(def, this);
//        return this;
//    }
//
//
//}
