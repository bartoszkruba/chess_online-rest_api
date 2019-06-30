package com.company.chess_online_bakend_api.controller;

import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping(AuthenticationController.BASE_URL)
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public static final String BASE_URL = "/auth/";

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("role")
    @ResponseStatus(HttpStatus.OK)
    public Collection<String> getRoles(Principal principal) {

        return authenticationService.getRolesForUser(principal.getName());
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCommand registerNewUser(@Valid @RequestBody UserCommand userCommand) {
        return authenticationService.registerNewUser(userCommand);
    }

}
