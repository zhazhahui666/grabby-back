package com.zzh.grabby.config.security;

import com.zzh.grabby.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author zzh
 * @date 2018/10/16
 */
public class SecurityUser extends User implements UserDetails {

    private static final long serialVersionUID = -5261927339155188116L;

    public SecurityUser(User user) {
        if (user != null) {
            BeanUtils.copyProperties(user, this);
        }
    }

    /**
     * 自定义了abacService校验权限，此处不处理
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


    /**
     * 账户过期 true正常，false过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户锁定
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    /**
     * 密码过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否启用
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
