/*
 * 7/27/19 3:26 PM. Updated by Bartosz Kruba.
 */

/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.command.game;

import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.converter.command.board.BoardCommandToBoard;
import com.company.chess_online_bakend_api.data.converter.command.user.UserCommandToUser;
import com.company.chess_online_bakend_api.data.model.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GameCommandToGame implements Converter<GameCommand, Game> {

    private final BoardCommandToBoard boardCommandToBoard;
    private final UserCommandToUser userCommandToUser;

    @Autowired
    public GameCommandToGame(BoardCommandToBoard boardCommandToBoard, UserCommandToUser userCommandToUser) {
        this.boardCommandToBoard = boardCommandToBoard;
        this.userCommandToUser = userCommandToUser;
    }

    @Override
    @Nullable
    public Game convert(GameCommand gameCommand) {

        log.debug("Converting GameCommand to Game");

        if (gameCommand == null) {
            return null;
        }

        var game = Game.builder()
                .id(gameCommand.getId())
                .turn(gameCommand.getTurn())
                .fenNotation(gameCommand.getFenNotation())
                .isKingAttacked(gameCommand.getIsKingAttacked())
                .isCheckmate(gameCommand.getIsCheckmate())
                .isDraw(gameCommand.getIsDraw())
                .status(gameCommand.getStatus()).build();

        if (gameCommand.getWhitePlayer() != null) {
            game.setWhitePlayer(userCommandToUser.convert(gameCommand.getWhitePlayer()));
        }

        if (gameCommand.getBlackPlayer() != null) {
            game.setBlackPlayer(userCommandToUser.convert(gameCommand.getBlackPlayer()));
        }

        if (gameCommand.getBoard() != null) {
            game.setBoard(boardCommandToBoard.convert(gameCommand.getBoard()));
        }

        return game;
    }
}
