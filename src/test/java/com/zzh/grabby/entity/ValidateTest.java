package com.zzh.grabby.entity;

import com.zzh.grabby.GrabbyApplicationTests;
import org.junit.Test;

import javax.validation.Valid;

/**
 * @author zzh
 * @date 2019/1/9
 */
public class ValidateTest extends GrabbyApplicationTests {


    public void addUser(@Valid TestUser user) {
        System.out.println(user);
    }

    @Test
    public void testAddUser() {
        TestUser user = new TestUser();
        user.setUsername("chen");
        user.setPassword("123");
        addUser(user);
    }
}
