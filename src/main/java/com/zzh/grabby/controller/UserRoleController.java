package com.zzh.grabby.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzh.grabby.common.dto.ResultDto;
import com.zzh.grabby.entity.UserRole;
import com.zzh.grabby.service.UserRoleService;
import com.zzh.grabby.common.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzh
 * @since 2018-10-18
 */
@RestController
@RequestMapping("/user-role")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @GetMapping("/delete-primary/{id}")
    public ResultDto deleteById(@PathVariable("id") Integer id) {
        boolean res = userRoleService.removeById(id);
        if (res) {
            return ResultUtil.setSuccessMsg("ok");
        } else {
            return ResultUtil.setErrorMsg("记录不存在");
        }
    }


    @GetMapping("/delete/{roleId}/{uid}")
    public ResultDto delete(@PathVariable("roleId") Integer roleId, @PathVariable("uid") Integer uid) {
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId);
        userRole.setUid(uid);
        wrapper.setEntity(userRole);

        boolean res = userRoleService.remove(wrapper);
        if (res) {
            return ResultUtil.setSuccessMsg("ok");
        } else {
            return ResultUtil.setErrorMsg("记录不存在");
        }
    }

    /**
     * 角色添加成员
     * @param roleId
     * @param uids
     * @return
     */
    @PostMapping("/add")
    public ResultDto add(@RequestParam int roleId,
                         @RequestParam Integer[] uids) {


        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);

        List<UserRole> list = userRoleService.list(wrapper);
        List<UserRole> saveList = new ArrayList<>();
        //分离出uids中有的，数据库中没有的进行添加
        for (Integer uid : uids) {
            boolean isExist = false;
            for (UserRole ur : list) {
                if (ur.getUid().equals(uid)) {
                    isExist = true;
                    break;
                }
            }
            if (isExist == false) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(roleId);
                userRole.setUid(uid);
                saveList.add(userRole);
            }
        }
        userRoleService.saveBatch(saveList);
        return ResultUtil.setSuccess();
    }

//    public static void main(String[] args) {
//
//        //要的是1中存在，2中不存在
//        Integer[] id1s = {1, 2, 3, 4, 5, 6, 7, 8, 9}; //待插入
//        Integer[] id2s = {};  //数据库
//
//        List<Integer> list = new ArrayList<>();
//        for (Integer id1 : id1s) {
//            boolean isExist = false;
//            for (Integer id2 : id2s) {
//                if (id1.equals(id2)) {
//                    isExist = true;
//                    break;
//                }
//            }
//            if (isExist == false) {
//                list.add(id1);
//            }
//        }
//
//        System.out.println(list);
//
//    }


}
