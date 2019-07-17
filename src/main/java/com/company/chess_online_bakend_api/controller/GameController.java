package com.company.chess_online_bakend_api.controller;

import com.company.chess_online_bakend_api.controller.propertyEditor.PieceColorPropertyEditor;
import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.service.GameService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping(GameController.BASE_URL)
public class GameController {
    public static final String BASE_URL = "/game/";

    private final GameService gameService;
    private final PieceColorPropertyEditor pieceColorPropertyEditor;

    @Autowired
    public GameController(GameService gameService, PieceColorPropertyEditor pieceColorPropertyEditor) {
        this.gameService = gameService;
        this.pieceColorPropertyEditor = pieceColorPropertyEditor;
    }

    @InitBinder
    public void initBinder(final WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(PieceColor.class, pieceColorPropertyEditor);
    }

    @ApiOperation(value = "Get game by id",
            notes = "Return 404 NOT FOUND if game with given id does not exist")
    @GetMapping({"{id}", "{id}/"})
    @ResponseStatus(HttpStatus.OK)
    public GameCommand getGameById(@PathVariable Long id) {
        log.debug("New request: GET " + BASE_URL + id);

        return gameService.findById(id);
    }

    @ApiOperation(value = "Join game",
            notes = "Can choose between white and black place \n" +
                    "Returns 404 NOT FOUND if game with given id does not exist \n" +
                    "Returns 400 BAD REQUEST if place is already taken or game already stared \n" +
                    "Restricted to logged users")
    @PutMapping({"{id}/join/{color}", "{id}/join/{color}/"})
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public GameCommand joinGame(@PathVariable Long id, @PathVariable PieceColor color, Principal principal) {
        log.debug("New request: PUT " + BASE_URL + id + "/join/" + color);


        // TODO: 2019-07-17 handle case when player already joined game
        return gameService.joinGame(color, principal.getName(), id);
    }
}
