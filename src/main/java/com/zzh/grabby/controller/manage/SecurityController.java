package com.zzh.grabby.controller.manage;

import com.zzh.grabby.common.util.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zzh
 * @date 2018/10/11
 */
@RestController
public class SecurityController {


    @GetMapping("/grabby/not_login")
    public Object notLogin(){
        return ResultUtil.setErrorMsg(401,"请先登录");
    }

}
