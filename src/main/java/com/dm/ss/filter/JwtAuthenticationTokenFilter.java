package com.dm.ss.filter;

import com.dm.ss.domain.LoginUser;
import com.dm.ss.utils.JwtUtil;
import com.dm.ss.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//继承OncePerRequestFilter，防止过滤器被多次调用
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = httpServletRequest.getHeader("token");
        //如果token不为空
        if (StringUtils.hasText(token)) {
            String userId;
            try {
                //解析token获取原id
                Claims parseJWT = JwtUtil.parseJWT(token);
                userId = parseJWT.getSubject();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("token非法");
            }
            //从redis中获取用户信息
            LoginUser loginUser = redisCache.getCacheObject("login_" + userId);
            if (ObjectUtils.isEmpty(loginUser)) {
                throw new RuntimeException("用户未登录");
            }

            //将loginUser和权限信息存入SecurityContextHolder
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        //放行过滤链
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
