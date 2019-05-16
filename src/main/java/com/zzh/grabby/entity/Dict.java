package com.zzh.grabby.entity;
import java.util.List;
import com.zzh.grabby.entity.Dict;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzh.grabby.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzh
 * @since 2019-01-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_dict")
public class Dict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "字典名称不能为空")
    private String name;

    /**
     * 字典code
     */
    @NotEmpty(message = "字典code不能为空")
    private String dictCode;

    private Double rank;

    private String description;


}
