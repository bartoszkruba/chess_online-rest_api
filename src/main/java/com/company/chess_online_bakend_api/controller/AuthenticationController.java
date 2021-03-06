/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.controller;

import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.service.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;

//import static org.springframework.security.config.annotation.web.configurers.UrlAuthorizationConfigurer.hasAnyRole;

@Api
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

    @ApiOperation(value = "Get list of your accounts roles",
            notes = "Works only for logged in users")
    @GetMapping("role")
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public Collection<String> getRoles(Principal principal) {
        log.debug("New request: GET " + BASE_URL + "role");

        return authenticationService.getRolesForUser(principal.getName());
    }

    @ApiOperation(value = "Register new account for service.",
            notes = "Username must be unique\n" +
                    "Username and password must be between 3 and 30 characters long.\n" +
                    "New registered accounts have basic \"USER\" role.")
    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCommand registerNewUser(@Valid @RequestBody UserCommand userCommand) {
        log.debug("New request: POST " + BASE_URL + "register");

        return authenticationService.registerNewUser(userCommand);
    }

}
