package com.zzh.grabby.activiti.service;

import com.zzh.grabby.activiti.entity.ActProcess;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzh
 * @since 2019-01-06
 */
public interface ActProcessService extends IService<ActProcess> {

    void addProcess(Integer categoryId, String processName);
}
