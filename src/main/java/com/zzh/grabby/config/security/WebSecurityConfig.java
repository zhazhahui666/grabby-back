package com.zzh.grabby.config.security;

import com.zzh.grabby.config.filestorage.IgnoredUrlProperties;
import com.zzh.grabby.config.security.authentication.jwt.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.List;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private AuthenticationSuccessHandler successHandler;


    @Autowired
    private AuthenticationFailureHandler failureHandler;


    @Autowired
    private SecurityAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private IgnoredUrlProperties ignoredUrlProperties;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();

        //放行的url
        List<String> urls = ignoredUrlProperties.getUrls();
        if (urls != null && urls.size() > 0) {
            for (String url : urls) {
                registry.antMatchers(url).permitAll();
            }
        }

        registry.and()
                .formLogin()
                .loginPage("/grabby/not_login")
                .loginProcessingUrl("/user/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                //允许网页iframe
                .headers().frameOptions().disable()
                .and()
                .csrf().disable()
                //前后端分离采用JWT 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .anyRequest()
                //权限sevice
                .access("@rbacService.hasPermission(request, authentication)")
                .and()
                //自定义权限拒绝处理类
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                //添加jwt过滤器
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), userDetailsService));

    }
}
