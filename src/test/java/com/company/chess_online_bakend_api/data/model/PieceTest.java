package com.company.chess_online_bakend_api.data.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PieceTest {

    @Test
    void increaseMoveCountValueNull() {
        Piece piece = Piece.builder().build();

        piece.increaseMoveCount();

        assertEquals(Integer.valueOf(1), piece.getMoves());
    }

    @Test
    void increaseMoveCount() {
        Piece piece = Piece.builder().moves(2).build();

        piece.increaseMoveCount();

        assertEquals(Integer.valueOf(3), piece.getMoves());
    }
}