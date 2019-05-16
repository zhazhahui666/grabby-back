package com.zzh.grabby.config.security.authentication.jwt;

import com.zzh.grabby.common.util.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)  {

        //TODO 记录失败次数
        ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "用户名或密码错误"));
    }
}
