package com.company.chess_online_bakend_api.data.converter.move;

import com.company.chess_online_bakend_api.data.command.MoveCommand;
import com.company.chess_online_bakend_api.data.model.Move;
import com.company.chess_online_bakend_api.data.model.enums.HorizontalPosition;
import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.model.enums.PieceType;
import com.company.chess_online_bakend_api.data.model.enums.VerticalPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MoveCommandToMoveTest {

    private final Long MOVE_ID = 1L;
    private final Integer COUNT = 7;
    private final LocalDateTime CREATION_TIME = LocalDateTime.now();

    @InjectMocks
    MoveCommandToMove moveCommandToMove;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testConvertNull() {
        assertNull(moveCommandToMove.convert(null));
    }

    @Test
    void testConvertEmptyObject() {
        Move move = moveCommandToMove.convert(MoveCommand.builder().build());

        assertNull(move.getId());
        assertNull(move.getCreated());
        assertNull(move.getMoveCount());
        assertNull(move.getPieceColor());
        assertNull(move.getPieceType());
        assertNull(move.getHorizontalStartPosition());
        assertNull(move.getHorizontalEndPosition());
        assertNull(move.getVerticalStartPosition());
        assertNull(move.getVerticalEndPosition());
        assertNull(move.getIsKingSideCastle());
        assertNull(move.getIsQueenSideCastle());
        assertNull(move.getIsKingAttacked());
        assertNull(move.getIsCheckmate());
        assertNull(move.getIsDraw());
    }

    @Test
    void testConvert() {
        MoveCommand moveCommand = MoveCommand.builder()
                .id(MOVE_ID)
                .count(COUNT)
                .happenedOn(CREATION_TIME)
                .pieceColor(PieceColor.BLACK)
                .pieceType(PieceType.BISHOP)
                .isKingSideCastle(true)
                .isQueenSideCastle(true)
                .isKingAttacked(true)
                .isCheckmate(true)
                .isDraw(true)
                .from("A1")
                .to("B2").build();

        Move move = moveCommandToMove.convert(moveCommand);

        assertEquals(MOVE_ID, move.getId());
        assertEquals(COUNT, move.getMoveCount());
        assertEquals(CREATION_TIME, move.getCreated());
        assertEquals(PieceColor.BLACK, move.getPieceColor());
        assertEquals(PieceType.BISHOP, move.getPieceType());
        assertEquals(HorizontalPosition.A, move.getHorizontalStartPosition());
        assertEquals(VerticalPosition.ONE, move.getVerticalStartPosition());
        assertEquals(HorizontalPosition.B, move.getHorizontalEndPosition());
        assertEquals(VerticalPosition.TWO, move.getVerticalEndPosition());
        assertEquals(true, move.getIsKingSideCastle());
        assertEquals(true, move.getIsQueenSideCastle());
        assertEquals(true, move.getIsKingAttacked());
        assertEquals(true, move.getIsCheckmate());
        assertEquals(true, move.getIsDraw());
    }
}