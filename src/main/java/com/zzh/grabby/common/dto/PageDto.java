package com.zzh.grabby.common.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author zzh
 * @date 2018/10/28
 */
@Data
public class PageDto {

    @NotNull(message = "当前页不能为空")
    @Min(value = 1,message = "页码必须大于1")
    private Integer pageNumber;

    @NotNull(message = "分页大小不能为空")
    @Min(value = 10,message = "分页大小必须大于10")
    private Integer pageSize;

    //排序字段
    private String sortColumn;

    private Boolean isAsc;

}
