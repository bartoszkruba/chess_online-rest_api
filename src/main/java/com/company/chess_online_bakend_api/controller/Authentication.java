package com.company.chess_online_bakend_api.controller;

import com.company.chess_online_bakend_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class Authentication {

    private final UserService userService;

    @Autowired
    public Authentication(UserService userService) {
        this.userService = userService;
    }
}
