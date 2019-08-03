/*
 * 8/3/19, 3:15 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.notification.move;

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
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MoveToMoveNotificationTest {

    private final Long MOVE_ID = 1L;
    private final Integer COUNT = 7;
    private final LocalDateTime CREATION_TIME = LocalDateTime.now();

    @InjectMocks
    private MoveToMoveNotification moveToMoveNotification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testConvertNull() {
        assertNull(moveToMoveNotification.convert(null));
    }

    @Test
    void testConvertEmptyObject() {

        var moveNotification = moveToMoveNotification.convert(Move.builder().build());

        assertNull(moveNotification.getId());
        assertNull(moveNotification.getCount());
        assertNull(moveNotification.getTimestamp());
        assertNull(moveNotification.getFrom());
        assertNull(moveNotification.getTo());
        assertNull(moveNotification.getPieceColor());
        assertNull(moveNotification.getPieceType());
        assertNull(moveNotification.getIsKingSideCastle());
        assertNull(moveNotification.getIsQueenSideCastle());
        assertNull(moveNotification.getIsKingAttacked());
        assertNull(moveNotification.getIsCheckmate());
        assertNull(moveNotification.getIsDraw());
    }

    @Test
    void testConvert() {
        var move = Move.builder().id(MOVE_ID)
                .moveCount(COUNT)
                .created(CREATION_TIME)
                .horizontalStartPosition(HorizontalPosition.A)
                .verticalStartPosition(VerticalPosition.ONE)
                .horizontalEndPosition(HorizontalPosition.B)
                .verticalEndPosition(VerticalPosition.TWO)
                .pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.QUEEN)
                .isKingSideCastle(true)
                .isQueenSideCastle(true)
                .isKingAttacked(true)
                .isCheckmate(true)
                .isDraw(true)
                .build();

        var moveNotification = moveToMoveNotification.convert(move);

        Long time = CREATION_TIME.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        assertEquals(MOVE_ID, moveNotification.getId());
        assertEquals(COUNT, moveNotification.getCount());
        assertEquals(time, moveNotification.getTimestamp());
        assertEquals("A1", moveNotification.getFrom());
        assertEquals("B2", moveNotification.getTo());
        assertEquals(PieceColor.WHITE, moveNotification.getPieceColor());
        assertEquals(PieceType.QUEEN, moveNotification.getPieceType());
        assertEquals(true, moveNotification.getIsKingSideCastle());
        assertEquals(true, moveNotification.getIsQueenSideCastle());
        assertEquals(true, moveNotification.getIsKingAttacked());
        assertEquals(true, moveNotification.getIsCheckmate());
        assertEquals(true, moveNotification.getIsDraw());
    }
}