package com.zzh.grabby.config.security.authentication.jwt;

import com.zzh.grabby.common.constant.SecurityConstant;
import com.zzh.grabby.exception.GrabbyException;
import com.zzh.grabby.common.util.ResponseUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zzh
 * @date 2018/10/12
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    private UserDetailsService userDetailsService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        //请求头中获取
        String token = request.getHeader(SecurityConstant.HEADER);
        if(StringUtils.isBlank(token)){ //参数中获取
            token = request.getParameter(SecurityConstant.HEADER);
        }
        //Cookie中获取
        if(StringUtils.isBlank(token)){
            Cookie[] cookies = request.getCookies();
            if(cookies != null && cookies.length>0){
               for(Cookie cookie:cookies){
                   if(SecurityConstant.HEADER.equals(cookie.getName())){
                       token = cookie.getValue();
                   }
               }
            }
        }
        if (StringUtils.isBlank(token)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(token, response);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        chain.doFilter(request, response);
    }


    private UsernamePasswordAuthenticationToken getAuthentication(String token, HttpServletResponse response) {

        if (StringUtils.isNotBlank(token)) {
            // 解析token
            Claims claims = null;
            try {
                claims = Jwts.parser()
                        .setSigningKey(SecurityConstant.JWT_SIGN_KEY)
                        .parseClaimsJws(token)
                        .getBody();

                //获取用户名
                String username = claims.getSubject();

                //获取权限
                List<GrantedAuthority> authorities = new ArrayList<>();

                if(StringUtils.isNotBlank(username)) {
//                    User principal = new User(username, "", AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER"));
                    UserDetails principal = userDetailsService.loadUserByUsername(username);
                    return new UsernamePasswordAuthenticationToken(principal, null, AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER"));
                }
            } catch (ExpiredJwtException e) {
                throw new GrabbyException("登录已失效，请重新登录");
            } catch (Exception e){
                ResponseUtil.out(response, ResponseUtil.resultMap(false,500,"解析token错误"));
            }
        }
        return null;
    }

}
