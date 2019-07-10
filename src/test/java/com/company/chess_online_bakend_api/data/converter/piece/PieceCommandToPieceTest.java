package com.company.chess_online_bakend_api.data.converter.piece;

import com.company.chess_online_bakend_api.data.command.PieceCommand;
import com.company.chess_online_bakend_api.data.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PieceCommandToPieceTest {

    @InjectMocks
    PieceCommandToPiece pieceCommandToPiece;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testConvertNull() {
        Piece piece = pieceCommandToPiece.convert(null);

        assertNull(piece);
    }

    @Test
    void testConvertEmptyObject() {
        Piece piece = pieceCommandToPiece.convert(PieceCommand.builder().build());

        assertNull(piece.getId());
        assertNull(piece.getPieceColor());
        assertNull(piece.getPieceType());
        assertNull(piece.getHorizontalPosition());
        assertNull(piece.getVerticalPosition());
    }


    @Test
    void testConvert() {
        PieceCommand pieceCommand = PieceCommand.builder()
                .id(1L)
                .pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.KING)
                .position("a1")
                .build();

        Piece piece = pieceCommandToPiece.convert(pieceCommand);

        assertEquals(Long.valueOf(1), piece.getId());
        assertEquals(PieceColor.WHITE, piece.getPieceColor());
        assertEquals(PieceType.KING, piece.getPieceType());
        assertEquals(HorizontalPosition.A, piece.getHorizontalPosition());
        assertEquals(VerticalPosition.ONE, piece.getVerticalPosition());
    }
}