package com.zzh.grabby.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzh.grabby.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzh
 * @since 2018-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_user_role")
public class UserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer uid;

    private Integer roleId;


}
