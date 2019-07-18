package com.company.chess_online_bakend_api.util;

import com.company.chess_online_bakend_api.data.model.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardUtilTest {

    @Test
    void initNewBoard() {
        Board board = BoardUtil.initNewBoard();
        assertEquals(32, board.getPieces().size());
    }
}