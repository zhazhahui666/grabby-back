package com.zzh.grabby.entity;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zzh
 * @date 2019/1/9
 */
@Data
public class TestUser {


    @NotNull(message = "id不能为空")
    private Integer id;

    @NotNull(message = "用户名不能为空")
    @Size(min = 2, max = 14)
    private String username;


    @Min(value = 6,message = "密码长度必须大于6位")
    private String password;


}
