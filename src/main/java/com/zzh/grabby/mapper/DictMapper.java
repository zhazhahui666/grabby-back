package com.zzh.grabby.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zzh.grabby.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzh.grabby.entity.DictOption;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zzh
 * @since 2019-01-23
 */
public interface DictMapper extends BaseMapper<Dict> {

    List<DictOption> getOptionByCode(@Param("code") String code);


    Dict findById(@Param("id")Integer id);



}

