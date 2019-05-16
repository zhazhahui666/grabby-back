package com.zzh.grabby.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zzh.grabby.common.util.SysContext;
import com.zzh.grabby.config.security.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author zzh
 * @date 2018/10/15
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        SecurityUser user = SysContext.getUser();

        if(user !=  null){
            metaObject.setValue("createUser", user.getId());
            metaObject.setValue("updateUser", user.getId());
        }
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser user = (SecurityUser) authentication.getPrincipal();

        //不同上面的set方法
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);

        if(user !=  null){
            this.setFieldValByName("updateUser", user.getId(), metaObject);
        }
    }

}
