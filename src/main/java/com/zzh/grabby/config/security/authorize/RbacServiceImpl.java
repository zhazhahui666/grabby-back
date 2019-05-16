package com.zzh.grabby.config.security.authorize;

import com.zzh.grabby.config.security.SecurityUser;
import com.zzh.grabby.entity.Permission;
import com.zzh.grabby.entity.Role;
import com.zzh.grabby.service.PermissionService;
import com.zzh.grabby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author zzh
 * @date 2018/10/14
 */
@Component("rbacService")
public class RbacServiceImpl implements RbacService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();


    private Class<? extends Authentication> anonymousClass = AnonymousAuthenticationToken.class;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;


    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {

        //判断用户是否认证，AffirmativeBased.decide会调用本方法
        if (this.isAnonymous(authentication)) return false;

        //所有需要控制的权限
        List<Permission> permissions = permissionService.list(null);


//        User detail = (User) authentication.getPrincipal();
//        com.zzh.grabby.entity.User user = userService.findByUsername(detail.getUsername());
        SecurityUser user = (SecurityUser) authentication.getPrincipal();

        for (Role role : user.getRoles()) {
            if (role.getId().equals(1)) { //超级管理员
                return true;
            }
        }

        boolean hasPermission = true;
        //请求路径
        String requestURI = request.getRequestURI();

        //不在控制权限表中的放行
        for (Permission p : permissions) {
            //在权限表中
            if (!StringUtils.isEmpty(p.getPath()) && antPathMatcher.match(p.getPath(), requestURI)) {
                hasPermission = false;
                // 用户权限
                List<Permission> userPermissions = user.getPermissions();
                if (userPermissions != null && userPermissions.size() > 0) {
                    //判断该用户是否有该url的权限

                    for (Permission p2 : userPermissions) {
                        if (antPathMatcher.match(p2.getPath(), requestURI)) {
                            hasPermission = true;
                        }
                    }
                }
            }
        }
        return hasPermission;
    }


    public boolean isAnonymous(Authentication authentication) {
        return this.anonymousClass != null && authentication != null ? this.anonymousClass.isAssignableFrom(authentication.getClass()) : false;
    }
}
