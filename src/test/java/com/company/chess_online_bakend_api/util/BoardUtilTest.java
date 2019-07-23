package com.company.chess_online_bakend_api.util;

import com.company.chess_online_bakend_api.data.model.Board;
import com.company.chess_online_bakend_api.data.model.Piece;
import com.company.chess_online_bakend_api.data.model.enums.HorizontalPosition;
import com.company.chess_online_bakend_api.data.model.enums.VerticalPosition;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BoardUtilTest {

    @Test
    void initNewBoard() {
        Board board = BoardUtil.initNewBoard();
        assertEquals(32, board.getPieces().size());
    }

    @Test
    void movePiece() {
        Board board = BoardUtil.initNewBoard();

        Piece piece = board.getPieces()
                .stream()
                .filter(p -> p.getVerticalPosition() == VerticalPosition.TWO)
                .filter(p -> p.getHorizontalPosition() == HorizontalPosition.D)
                .findFirst().orElseThrow(RuntimeException::new);

        BoardUtil.movePiece(piece, board, "D7");

        assertEquals(HorizontalPosition.D, piece.getHorizontalPosition());
        assertEquals(VerticalPosition.SEVEN, piece.getVerticalPosition());

        assertEquals(31, board.getPieces().size());

        List<Piece> pieceList = board.getPieces()
                .stream()
                .filter(p -> p.getHorizontalPosition() == HorizontalPosition.D)
                .filter(p -> p.getVerticalPosition() == VerticalPosition.SEVEN)
                .collect(Collectors.toList());

        assertEquals(1, pieceList.size());
    }

    @Test
    void movePieceNullValues() {

        assertThrows(RuntimeException.class, () -> BoardUtil.movePiece(null, null, null));
    }
}