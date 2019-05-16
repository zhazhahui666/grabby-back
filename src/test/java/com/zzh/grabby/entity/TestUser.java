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

    @NotNull
    private Integer id;

    @NotNull
    @Size(min = 2, max = 14)
    private String username;


    @Min(6)
    private String password;


}
