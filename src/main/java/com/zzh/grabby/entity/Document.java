package com.zzh.grabby.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzh.grabby.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzh
 * @since 2019-01-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_document")
public class Document extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 正常
     */
    public final static Integer STATE_NORMAL = 1;

    /**
     * 草稿
     */
    public final static Integer STATE_DRAFT = 2;

    private String title;

    private String content;

    /**
     * 1=正常 2=草稿
     */
    private Integer state;

    private Integer directoryId;


}
