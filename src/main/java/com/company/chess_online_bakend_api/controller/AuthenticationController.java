package com.company.chess_online_bakend_api.controller;

import com.company.chess_online_bakend_api.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping(AuthenticationController.BASE_URL)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public static final String BASE_URL = "/auth/";

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("role")
    public Collection<String> getRoles(Principal principal) {

        return authenticationService.getRolesForUser(principal.getName());
    }
}
