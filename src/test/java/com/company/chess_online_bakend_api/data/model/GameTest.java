package com.company.chess_online_bakend_api.data.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTest {

    @Test
    void addMoveNullValuePassed() {
        Game game = Game.builder().build();

        assertThrows(RuntimeException.class, () -> game.addMove(null));
    }

    @Test
    void addMoveMovesNotInitialized() {
        Game game = Game.builder().build();

        game.addMove(Move.builder().build());

        assertEquals(1, game.getMoves().size());
    }

    @Test
    void addMove() {
        List<Move> moves = new ArrayList<>();
        moves.add(Move.builder().build());

        Game game = Game.builder().moves(moves).build();

        game.addMove(Move.builder().build());

        assertEquals(2, game.getMoves().size());
    }

    @Test
    void increaseTurnCountNullCount() {
        Game game = Game.builder().build();

        game.increaseTurnCount();

        assertEquals(Integer.valueOf(2), game.getTurn());
    }

    @Test
    void inscreaseTurnCount() {
        Game game = Game.builder().turn(7).build();

        game.increaseTurnCount();

        assertEquals(Integer.valueOf(8), game.getTurn());
    }
}