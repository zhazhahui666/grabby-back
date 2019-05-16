package com.zzh.grabby.common.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zzh
 * @date 2018/12/26
 */
@Data
public class PageVo<T> implements Serializable {


    private static final long serialVersionUID = -4693234149217805175L;

    private long current;

    private long page;

    private List<T> records;

    private long size;

    private long total;

}
