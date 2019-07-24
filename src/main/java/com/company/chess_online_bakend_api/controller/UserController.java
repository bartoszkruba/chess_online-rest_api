package com.company.chess_online_bakend_api.controller;

import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

    public static final String BASE_URL = "/user/";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get user by id",
            notes = "Returns 404 NOT FOUND if user does not exist")
    @GetMapping({"{id}", "/{id}/"})
    @ResponseStatus(HttpStatus.OK)
    public UserCommand getUserById(@PathVariable Long id) {
        log.debug("New request: GET " + BASE_URL + id);

        return userService.findById(id);
    }

    @ApiOperation(value = "Get user by username",
            notes = "Returns 404 NOT FOUND if user does not exist")
    @GetMapping({"username/{username}", "username/{username}"})
    @ResponseStatus(HttpStatus.OK)
    public UserCommand getUserByUsername(@PathVariable String username) {
        log.debug("New request: GET " + BASE_URL + "username/" + username);

        return userService.findByUsername(username);
    }
}
