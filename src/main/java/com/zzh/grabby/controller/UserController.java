package com.zzh.grabby.controller;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.common.constant.CommonConstant;
import com.zzh.grabby.common.dto.PageDto;
import com.zzh.grabby.common.dto.ResultDto;
import com.zzh.grabby.common.util.Jsr303Util;
import com.zzh.grabby.common.util.ResultUtil;
import com.zzh.grabby.common.util.SysContext;
import com.zzh.grabby.common.vo.ExcelErrorVo;
import com.zzh.grabby.common.vo.ResponseCode;
import com.zzh.grabby.entity.Department;
import com.zzh.grabby.entity.User;
import com.zzh.grabby.entity.UserExcel;
import com.zzh.grabby.entity.UserVo;
import com.zzh.grabby.mapper.UserMapper;
import com.zzh.grabby.service.DepartmentService;
import com.zzh.grabby.service.RoleService;
import com.zzh.grabby.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzh
 * @since 2018-10-16
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DepartmentService departmentService;


    @GetMapping("/get_user_info")
    public ResultDto getUserInfo() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = userService.findByUsername(user.getUsername());
        u.setPassword(null);
        return ResultUtil.setData(u);
    }


    /**
     * 人员管理分页数据
     *
     * @param user
     * @param pageDto
     * @return
     */
    @GetMapping("/get_page")
    public ResultDto getPage(@ModelAttribute User user,
                             @ModelAttribute PageDto pageDto) {
        Page<UserVo> page = new Page<>(pageDto.getPageNumber(), pageDto.getPageSize());
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        //TODO 代码优化，去掉很多的if,是否可以用注解
//        if (!StringUtils.isEmpty(user.getUsername())) {
//            wrapper.like("username", user.getUsername());
//        }
//        if (!StringUtils.isEmpty(user.getMobile())) {
//            wrapper.like("mobile", user.getMobile());
//        }
//        if (user.getDepartmentId() != null) {
//            wrapper.eq("department_id", user.getDepartmentId());
//        }
//        wrapper.orderByDesc("update_time");
//        IPage<User> pageUser = userService.page(page, wrapper);
        IPage<UserVo> pageUserVo = userService.selectUserPage(page, user);
        return ResultUtil.setData(pageUserVo);
    }


    /**
     * 添加人员
     *
     * @param user
     * @return
     */
    @PostMapping("/add")
    public ResultDto add(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return ResultUtil.setSuccessMsg("ok");
    }


    /**
     * 重置密码
     *
     * @param uid
     * @return
     */
    @GetMapping("/reset_password/{uid}")
    public ResultDto add(@PathVariable Integer uid) {
        User user = userService.getById(uid);
        if (user != null) {
            user.setPassword(passwordEncoder.encode("123456"));
            userService.updateById(user);
            return ResultUtil.setSuccessMsg("ok");
        } else {
            return ResultUtil.setErrorMsg("用户不存在");
        }
    }


    /**
     * 个人信息修改
     *
     * @param user
     * @return
     */
    @PostMapping("/self-update")
    public ResultDto updateSelf(@ModelAttribute User user) {
        User selfUser = SysContext.getUser();
        selfUser.setAvatar(user.getAvatar());
        selfUser.setSex(user.getSex());
        selfUser.setEmail(user.getEmail());
        selfUser.setNickname(user.getNickname());

        //手机是否有必要严谨点
        selfUser.setMobile(user.getMobile());
        userService.updateById(selfUser);
        return ResultUtil.setSuccess();
    }

    /**
     * 人员导入
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/import")
    public ResultDto importUser(@RequestParam("file") MultipartFile file) throws Exception {

        if (file == null || file.getInputStream() == null) {
            log.error("文件上传失败");
            return ResultUtil.setErrorMsg("上传文件失败");
        }
        //判断后缀
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            log.error("人员导入失败,文件类型不符,请上传xlsx或者xls文件");
            return ResultUtil.setErrorMsg("人员导入失败,文件类型不符,请上传xlsx或者xls文件");
        }
        //文件大小限制

        //解析文件成对象
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setStartSheetIndex(1);
        List<UserExcel> list = ExcelImportUtil.importExcel(
                file.getInputStream(),
                UserExcel.class, params);
        //校验对象
        List<User> userList = new ArrayList<>();
        List<ExcelErrorVo> errorList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String result = Jsr303Util.check(list.get(i));

            ExcelErrorVo error = new ExcelErrorVo();
            error.setRow(i + 2);
            //普通校验
            if (!StringUtils.isEmpty(result)) { //有错误
                error.setMessage(result);
            }

            //用户名校验
            if (userService.findByUsername(list.get(i).getUsername()) != null) {
                error.setMessage(list.get(i).getUsername() + "用户名已存在");
            }
            //部门简单校验
            String[] departmentNames = list.get(i).getDepartmentName().split(">");
            Department department = null;
            Department parentDep = null;
            for (int j = 0; j < departmentNames.length; j++) {
                department = departmentService.findByNameAndPid(departmentNames[j],parentDep==null?null:parentDep.getId());
                if (department == null) { //不存在的部门则添加
                    department = new Department();
                    department.setName(departmentNames[j]);
                    if (parentDep == null) { //分部
                        department.setPid(CommonConstant.ROOT_DEPARTMENT_ID);
                    } else { //分部下的部门
                        department.setPid(parentDep.getId());
                    }
                    departmentService.save(department);
                }
                parentDep = department;
            }


            //添加进结果集
            if (!StringUtils.isEmpty(error.getMessage())) {
                errorList.add(error);
            } else {
                //组装user
                User user = new User();
                BeanUtils.copyProperties(list.get(i), user);
                String passowrd = passwordEncoder.encode(list.get(i).getPassword());

                user.setStatus(CommonConstant.USER_STATUS_NORMAL);
                user.setPassword(passowrd);
                userList.add(user);
            }
        }
        if (errorList.size() > 0) {
            return ResultUtil.setData(ResponseCode.IMPORT_USER_ERROR, "导入失败", errorList);
        }

        //插入数据库
        userService.saveBatch(userList);
        return ResultUtil.setSuccessMsg("成功导入"+userList.size()+"条数据");
    }

    @GetMapping("/download-import-template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        String fileName = "人员导入模板.xlsx";
        fileName = URLEncoder.encode(fileName, "UTF-8");
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("download/人员导入模板.xlsx");
        // 设置输出的格式
        response.setHeader("Content-Disposition",
                "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO-8859-1"));
        response.setContentType("application/octet-stream; charset=utf-8");
        // 循环取出流中的数据
        ServletOutputStream out = response.getOutputStream();
        byte[] b = new byte[1024];
        int len;
        while ((len = in.read(b)) > 0) out.write(b, 0, len);
    }


//    /**
//     * 查询部门人员
//     * @param departmentId
//     * @return
//     */
//    @GetMapping("/get_department_users")
//    public ResultDto getDepartmentUsers(@RequestParam Integer departmentId){
//        List<User> list = userService.list(new QueryWrapper<>().eq("department_id", departmentId);
//        return ResultUtil.setData(list);
//    }

}
