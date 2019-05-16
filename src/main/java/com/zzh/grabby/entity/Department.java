package com.zzh.grabby.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzh.grabby.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzh
 * @since 2018-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_department")
@ToString
public class Department extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "部门名称不能为空")
    private String name;

    private Integer pid;

    private String description;

    /**
     * 排序
     */
    private Double rank;

    @TableField(exist = false)
    private List<Department> children = new ArrayList<>();


}
