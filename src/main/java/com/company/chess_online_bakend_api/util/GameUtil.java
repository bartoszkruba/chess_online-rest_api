package com.company.chess_online_bakend_api.util;

import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import com.github.bhlangonijr.chesslib.Board;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Slf4j
public class GameUtil {

    public static Game initNewGame() {
        log.debug("Initializing new game");

        return Game.builder()
                .status(GameStatus.WAITNG_TO_START)
                .turn(0)
                .fenNotation(new Board().getFen())
                .board(BoardUtil.initNewBoard())
                .build();
    }

    public static Game initNewGameBetweenPlayers(@NotNull User whitePlayer, @NotNull User blackPlayer) {
        log.debug("Initializing new game between: " + whitePlayer + " and " + blackPlayer);

        if (whitePlayer == null || blackPlayer == null) {
            log.error("One of players is null");
            throw new NullPointerException();
        }

        Game game = initNewGame();
        game.setWhitePlayer(whitePlayer);
        game.setBlackPlayer(blackPlayer);

        return game;
    }
}
