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

class MoveToMoveCommandTest {

    private final Long MOVE_ID = 1L;
    private final Integer COUNT = 7;
    private final LocalDateTime CREATION_TIME = LocalDateTime.now();

    @InjectMocks
    MoveToMoveCommand moveToMoveCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testConvertNull() {
        assertNull(moveToMoveCommand.convert(null));
    }

    @Test
    void testConvertEmptyObject() {

        MoveCommand moveCommand = moveToMoveCommand.convert(Move.builder().build());

        assertNull(moveCommand.getId());
        assertNull(moveCommand.getCount());
        assertNull(moveCommand.getHappenedOn());
        assertNull(moveCommand.getFrom());
        assertNull(moveCommand.getTo());
        assertNull(moveCommand.getPieceColor());
        assertNull(moveCommand.getPieceType());
    }

    @Test
    void testConvert() {
        Move move = Move.builder().id(MOVE_ID)
                .moveCount(COUNT)
                .created(CREATION_TIME)
                .horizontalStartPosition(HorizontalPosition.A)
                .verticalStartPosition(VerticalPosition.ONE)
                .horizontalEndPosition(HorizontalPosition.B)
                .verticalEndPosition(VerticalPosition.TWO)
                .pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.QUEEN)
                .build();

        MoveCommand moveCommand = moveToMoveCommand.convert(move);

        assertEquals(MOVE_ID, moveCommand.getId());
        assertEquals(COUNT, moveCommand.getCount());
        assertEquals(CREATION_TIME, moveCommand.getHappenedOn());
        assertEquals("A1", moveCommand.getFrom());
        assertEquals("B2", moveCommand.getTo());
        assertEquals(PieceColor.WHITE, moveCommand.getPieceColor());
        assertEquals(PieceType.QUEEN, moveCommand.getPieceType());
    }
}