package com.company.chess_online_bakend_api.controller;

import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.service.GameService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(GameController.BASE_URL)
public class GameController {
    public static final String BASE_URL = "/game/";

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @ApiOperation(value = "Get game by id",
            notes = "Return 404 NOT FOUND if game with given id does not exist")
    @GetMapping({"{id}", "{id}/"})
    @ResponseStatus(HttpStatus.OK)
    public GameCommand getGameById(@PathVariable Long id) {
        log.debug("New request: GET " + BASE_URL + id);

        return gameService.findById(id);
    }
}
