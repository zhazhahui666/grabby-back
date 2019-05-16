package com.zzh.grabby.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzh.grabby.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

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
@TableName("t_document_directory")
public class DocumentDirectory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private Integer pid;

    @TableField(exist = false)
    private List<Document> documentList;

    @TableField(exist = false)
    private List<DocumentDirectory> children;



}
