package com.company.chess_online_bakend_api.util;

import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import com.github.bhlangonijr.chesslib.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameUtilTest {

    @Test
    void initNewGame() {
        Game game = GameUtil.initNewGame();

        assertEquals(GameStatus.WAITNG_TO_START, game.getStatus());
        assertEquals(Integer.valueOf(0), game.getTurn());
        assertEquals(new Board().getFen(), game.getFenNotation());
        assertNull(game.getRoom());
        assertNull(game.getWhitePlayer());
        assertNull(game.getBlackPlayer());
        assertNotNull(game.getBoard());
    }

    @Test
    void initNewGameBetweenPlayers() {
        User whiteUser = User.builder().id(1L).build();
        User blackUser = User.builder().id(2L).build();

        Game game = GameUtil.initNewGameBetweenPlayers(whiteUser, blackUser);

        assertEquals(GameStatus.WAITNG_TO_START, game.getStatus());
        assertEquals(Integer.valueOf(0), game.getTurn());
        assertEquals(new Board().getFen(), game.getFenNotation());
        assertNull(game.getRoom());
        assertEquals(whiteUser, game.getWhitePlayer());
        assertEquals(blackUser, game.getBlackPlayer());
        assertNotNull(game.getBoard());
    }
}