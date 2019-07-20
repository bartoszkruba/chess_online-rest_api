package com.company.chess_online_bakend_api.controller;

import com.company.chess_online_bakend_api.controller.propertyEditor.PieceColorPropertyEditor;
import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.command.MoveCommand;
import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.validation.group.OnGetPossibleMoves;
import com.company.chess_online_bakend_api.service.GameService;
import com.company.chess_online_bakend_api.service.MoveService;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(GameController.BASE_URL)
public class GameController {
    public static final String BASE_URL = "/game/";

    private final MoveService moveService;
    private final GameService gameService;
    private final PieceColorPropertyEditor pieceColorPropertyEditor;

    @Autowired
    public GameController(MoveService moveService, GameService gameService,
                          PieceColorPropertyEditor pieceColorPropertyEditor) {
        this.moveService = moveService;
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
            notes = "Can choose between white and black place. \n" +
                    "Returns 404 NOT FOUND if game with given id does not exist. \n" +
                    "Returns 400 BAD REQUEST if place is already taken or game already stared. \n" +
                    "Restricted to logged users.")
    @PutMapping({"{id}/join/{color}", "{id}/join/{color}/"})
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public GameCommand joinGame(@PathVariable Long id, @PathVariable PieceColor color, Principal principal) {
        log.debug("New request: PUT " + BASE_URL + id + "/join/" + color);

        return gameService.joinGame(color, principal.getName(), id);
    }

    @ApiOperation(value = "Leave game",
            notes = "Returns 404 NOT FOUNd if game with given id does not exist or you have not joined game. \n" +
                    "If game already started status will be changed to STOPPED and you will lose. \n " +
                    "Restricted to logged users")
    @PutMapping({"{id}/leave", "{id}/leave/"})
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public GameCommand leaveGame(@PathVariable Long id, Principal principal) {
        log.debug("New request: PUT " + BASE_URL + id + "/leave");

        return gameService.leaveGame(principal.getName(), id);
    }

    @ApiOperation(value = "Get possible moves from certain position",
            notes = "Returns 404 NOT FOUND if game with given id does not exist. \n" +
                    "Return all possible moves if there is no start position specified.")
    @GetMapping({"{id}/possibleMoves", "{id}/possibleMoves/"})
    @ResponseStatus(HttpStatus.OK)
    public Set<MoveCommand> getPossibleMoves(@PathVariable Long id,
                                             @Validated(OnGetPossibleMoves.class) @RequestBody MoveCommand moveCommand)
            throws MoveGeneratorException {
        log.debug("New request: GET " + BASE_URL + id + "/possibleMoves");

        return moveService.getPossibleMoves(moveCommand.getFrom(), id);
    }
}
