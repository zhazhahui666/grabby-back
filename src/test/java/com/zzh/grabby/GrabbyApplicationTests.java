package com.zzh.grabby;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.grabby.entity.Permission;
import com.zzh.grabby.entity.User;
import com.zzh.grabby.mapper.PermissionMapper;
import com.zzh.grabby.service.PermissionService;
import com.zzh.grabby.service.UserService;
import org.activiti.engine.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GrabbyApplicationTests {


    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PermissionMapper permissionMapper;
    
    @Autowired
    private UserService userService;

//    @Autowired
//    private TaskService taskService;


    @Test
    public void testInsert() {
        Permission p = new Permission();
        p.setId(11);
        permissionMapper.insert(p);
    }

    @Test
    public void testUpdate() {
        Permission p = permissionMapper.selectById("1");

        p.setPid("444");
        permissionMapper.updateById(p);
    }
    
    
    @Test
    public void testSelectAll(){
        List<Permission> list = permissionService.list(null);
        System.out.println(list.size());

    }


    @Test
    public void findPermissionByUid(){
        int uid = 1;
        List<Permission> list = permissionMapper.findByUid(uid);
        System.out.println(list.size());
    }
    
    @Test
    public  void userPage(){
        Page<User> p = new Page<>(1,5);
        IPage<User> page = userService.page(p, null);
        System.out.println(page);
    }

//    @Test
//    public void activitiTest(){
//        System.out.println(taskService);
//        System.out.println(taskService);
//    }


}
