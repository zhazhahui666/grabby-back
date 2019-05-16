package com.zzh.grabby.common.util;

import com.zzh.grabby.config.security.SecurityUser;
import com.zzh.grabby.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author zzh
 * @date 2018/12/26
 */
public class SysContext {

    public static SecurityUser getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
//        if("anonymousUser".equals(principal)){
//            return new SecurityUser(new User().setUsername("anonymousUser"));
//        }

        return (SecurityUser)principal;

    }
}
