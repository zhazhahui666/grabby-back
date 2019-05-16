package com.zzh.grabby.config.security.authentication.jwt;

import com.zzh.grabby.common.constant.SecurityConstant;
import com.zzh.grabby.common.util.ResponseUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();


        //TODO 判断token是否失效，注销用户同时注销token

        //登陆成功生成JWT
        String token = Jwts.builder()
                //主题 放入用户名
                .setSubject(username)
                //失效时间
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                //签名算法和密钥
                .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SIGN_KEY)
                .compact();
//        token = SecurityConstant.TOKEN_SPLIT + token;

        ResponseUtil.out(response, ResponseUtil.resultMap(true, 200, "登录成功", token));
    }
}
