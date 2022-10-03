package com.dm.ss.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dm.ss.domain.LoginUser;
import com.dm.ss.domain.User;
import com.dm.ss.mapper.MenuMapper;
import com.dm.ss.mapper.UserMapper;
import com.dm.ss.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义从数据库中进行查询
 */
@Service
public class UserDetailsServiceImpl implements UserDetailService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, username);

        User user = userMapper.selectOne(wrapper);

        //如果查询不到数据就通过抛出异常来给出提示
        if (ObjectUtils.isEmpty(user)) {
            throw new RuntimeException("用户名或密码错误");
        }

        //根据用户查询权限信息 添加到LoginUser中
        List<String> strings = menuMapper.selectPermsByUserId(user.getId());

        //封装成UserDetails对象返回
        return new LoginUser(user, strings);
    }
}