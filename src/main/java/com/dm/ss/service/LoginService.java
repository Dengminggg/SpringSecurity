package com.dm.ss.service;

import com.dm.ss.domain.ResponseResult;
import com.dm.ss.domain.User;

public interface LoginService {
    ResponseResult login(User user);
}
