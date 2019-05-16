package com.zzh.grabby.config.security.authorize;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zzh
 * @date 2018/10/14
 */
public interface RbacService {

    boolean hasPermission(HttpServletRequest request, Authentication authentication);

}
