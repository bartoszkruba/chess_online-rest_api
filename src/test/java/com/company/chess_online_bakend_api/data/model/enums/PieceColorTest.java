package com.company.chess_online_bakend_api.data.model.enums;

import com.company.chess_online_bakend_api.exception.InvalidPieceColorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PieceColorTest {

    @Test
    void fromValueWhite() throws InvalidPieceColorException {

        PieceColor pieceColor = PieceColor.fromValue("white");
        assertEquals(pieceColor, PieceColor.WHITE);
    }

    @Test
    void fromValueBlack() throws InvalidPieceColorException {
        PieceColor pieceColor = PieceColor.fromValue("BLACK");
        assertEquals(pieceColor, PieceColor.BLACK);
    }

    @Test
    void fromInvalidValue() {
        Assertions.assertThrows(InvalidPieceColorException.class, () -> {
            PieceColor pieceColor = PieceColor.fromValue("purple");
        });
    }
}