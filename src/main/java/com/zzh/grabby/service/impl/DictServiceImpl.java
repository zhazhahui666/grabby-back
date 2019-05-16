package com.zzh.grabby.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.entity.Dict;
import com.zzh.grabby.entity.DictOption;
import com.zzh.grabby.mapper.DictMapper;
import com.zzh.grabby.service.DictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzh
 * @since 2019-01-23
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;

//    @Override
//    public List<DictOption> getOptionByCode(String code) {
//        return dictMapper.getOptionByCode(code);
//    }

    @Override
    public List<DictOption> getOptionByCode( String code) {
       return dictMapper.getOptionByCode( code);
    }
}
