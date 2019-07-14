package com.company.chess_online_bakend_api.controller;

import com.company.chess_online_bakend_api.data.command.RoomCommand;
import com.company.chess_online_bakend_api.data.command.RoomCountCommand;
import com.company.chess_online_bakend_api.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api
@Slf4j
@RestController
@RequestMapping(RoomController.BASE_URL)
public class RoomController {

    private final RoomService roomService;

    public static final String BASE_URL = "/room/";

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @ApiOperation(value = "Get room by id",
            notes = "Returns 404 NOT FOUND if room with given id does not exist")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoomCommand getRoomById(@PathVariable Long id) {
        log.debug("New request: GET " + BASE_URL + id);

        return roomService.findById(id);
    }

    @ApiOperation(value = "Get rooms count")
    @GetMapping("count")
    @ResponseStatus(HttpStatus.OK)
    public RoomCountCommand getRoomCount() {
        log.debug("New request: GET " + BASE_URL + "count");

        return RoomCountCommand.builder().count(roomService.getRoomCount()).build();
    }

    @ApiOperation(value = "Create new room",
            notes = "Restricted for only admins. \n" +
                    "Returns 400 BAD REQUEST if room with same name already exists")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN"})
    public RoomCommand createNewRoom(@Valid @RequestBody RoomCommand roomCommand) {
        log.debug("New request: POST " + BASE_URL);

        return roomService.createNewRoom(roomCommand);
    }

}
