package com.zzh.grabby.config.security;

import com.zzh.grabby.entity.User;
import com.zzh.grabby.exception.GrabbyException;
import com.zzh.grabby.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * @author zzh
 * @date 2018/10/11
 */
@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {


//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userService.findByUsername(username);
        if(user == null){
            throw new GrabbyException("账号密码错误");
        }
        return new SecurityUser(user);
    }

}
