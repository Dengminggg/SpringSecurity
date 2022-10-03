package com.dm.ss.controller;

import com.dm.ss.domain.ResponseResult;
import com.dm.ss.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    @Autowired
    private LogoutService logoutService;

    @GetMapping("/user/logout")
    public ResponseResult logout() {
        return logoutService.logout();
    }
}
