package com.zzh.grabby.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zzh
 * @date 2019/2/12
 */
@Data
public class UserExcel {

    @Excel(name = "登录名",isImportField = "true_st")
    private String username;

    @Excel(name = "密码",isImportField = "true_st")
    @NotNull(message = "密码不能为空")
    private String password;

    private String nickname;

    @Excel(name = "姓名",isImportField = "true_st")
    @NotNull(message = "姓名不能为空")
    private String realname;

    @Excel(name = "手机",isImportField = "true_st")
    @NotNull(message = "手机不能为空")
    private String mobile;

    @Excel(name = "邮箱",isImportField = "true_st")
    private String email;

    @Excel(name = "地址",isImportField = "true_st")
    private String address;

    @Excel(name = "部门",isImportField = "true_st")
    @NotNull(message = "部门不能为空")
    private String departmentName;








}
