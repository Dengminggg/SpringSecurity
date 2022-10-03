package com.dm.ss.service.impl;


import com.dm.ss.domain.LoginUser;
import com.dm.ss.domain.ResponseResult;
import com.dm.ss.domain.User;
import com.dm.ss.service.LoginService;
import com.dm.ss.utils.JwtUtil;
import com.dm.ss.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;


//认证处理
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        /**
         *  使用authenticationManager的authenticate方法去进行用户认证
         *  principal：主要的
         *  credentials：凭证
         */

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authentication);

        //如果返回值为空，则是不正确的用户名或密码
        if (ObjectUtils.isEmpty(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }

        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String id = loginUser.getUser().getId().toString();
        String token = JwtUtil.createJWT(id);

        //authenticate存入redis
        redisCache.setCacheObject("login_" + id, loginUser);

        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);

        return new ResponseResult(200, "登陆成功", map);
    }
}
