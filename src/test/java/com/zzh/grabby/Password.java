package com.zzh.grabby;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author zzh
 * @date 2018/10/19
 */
public class Password {

    @Test
    public void generate(){
        BCryptPasswordEncoder be = new BCryptPasswordEncoder();
        String encode = be.encode("123456");
        System.out.println(encode);
    }
}
