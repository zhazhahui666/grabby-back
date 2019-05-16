package com.zzh.grabby.config.security;

import com.zzh.grabby.common.util.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zzh
 * @date 2018/10/16
 */
@Component
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {

        ResponseUtil.out(response, ResponseUtil.resultMap(false, 403, "抱歉，您没有访问权限"));
    }
}