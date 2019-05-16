package com.zzh.grabby.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzh.grabby.entity.DictOption;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzh
 * @since 2019-01-23
 */
public interface DictService extends IService<Dict> {
    List<DictOption> getOptionByCode(String code);

}
