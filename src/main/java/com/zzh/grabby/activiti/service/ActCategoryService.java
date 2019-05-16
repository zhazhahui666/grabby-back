package com.zzh.grabby.activiti.service;

import com.zzh.grabby.activiti.entity.ActCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzh
 * @since 2018-12-23
 */
public interface ActCategoryService extends IService<ActCategory> {

    List<ActCategory> getProcessCategoty(Integer processState);
}
