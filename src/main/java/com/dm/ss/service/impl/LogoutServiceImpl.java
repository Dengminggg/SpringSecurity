package com.dm.ss.service.impl;

import com.dm.ss.domain.LoginUser;
import com.dm.ss.domain.ResponseResult;
import com.dm.ss.service.LogoutService;
import com.dm.ss.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LogoutServiceImpl implements LogoutService {

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult logout() {
        //从SecurityContextHolder获取用户的id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUser().getId();

        //从redis删除id记录
        redisCache.deleteObject("login_" + id);

        //返回响应对象
        return new ResponseResult(200, "退出成功");
    }
}
