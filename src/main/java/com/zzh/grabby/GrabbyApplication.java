package com.zzh.grabby;

import com.zzh.grabby.entity.TestUser;
import lombok.extern.slf4j.Slf4j;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// Activiti5.22需要排除
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@RestController
@Slf4j
@Validated
public class GrabbyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrabbyApplication.class, args);
    }

    @GetMapping("/test")
    public Object test(Date date) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "chenliang111");
        map.put("date", new Date());
        log.info("info日志");
        log.warn("warn日志");
        log.debug("debug日志");
        log.error("erro日志");
        System.out.println(date);
        return map;
    }


    @GetMapping("/add-user-test")
    public void testAdd(@Valid @ModelAttribute TestUser user){
        System.out.println(user);
    }

    @GetMapping("/add-user-test2")
    public void testAdd2(@NotNull @Size(min = 6,message = "最小长度为6") String username,
                         @NotNull @Size(min = 6,message = "密码强度太低")String password){
        System.out.println(username);
    }

}
