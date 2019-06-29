package com.company.chess_online_bakend_api.controller;

import com.company.chess_online_bakend_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthenticationController.BASE_URL)
public class AuthenticationController {

    public static final String BASE_URL = "/auth/";

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }
}
