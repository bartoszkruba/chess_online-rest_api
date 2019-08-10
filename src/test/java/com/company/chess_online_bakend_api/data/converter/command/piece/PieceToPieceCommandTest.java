/*
 * 8/3/19, 3:17 PM. Updated by Bartosz Kruba.
 */

/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.command.piece;

import com.company.chess_online_bakend_api.data.command.PieceCommand;
import com.company.chess_online_bakend_api.data.model.Piece;
import com.company.chess_online_bakend_api.data.model.enums.HorizontalPosition;
import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.model.enums.PieceType;
import com.company.chess_online_bakend_api.data.model.enums.VerticalPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PieceToPieceCommandTest {

    @InjectMocks
    PieceToPieceCommand pieceToPieceCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testConvertNull() {

        PieceCommand pieceCommand = pieceToPieceCommand.convert(null);

        assertNull(pieceCommand);
    }

    @Test
    void testConvertEmptyObject() {

        PieceCommand pieceCommand = pieceToPieceCommand.convert(Piece.builder().build());

        assertNull(pieceCommand.getId());
        assertNull(pieceCommand.getPieceColor());
        assertNull(pieceCommand.getPieceType());
        assertNull(pieceCommand.getPosition());
        assertNull(pieceCommand.getMoves());
    }

    @Test
    void testConvert() {
        Piece piece = Piece.builder()
                .id(1L)
                .pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.KING)
                .moves(3)
                .horizontalPosition(HorizontalPosition.A)
                .verticalPosition(VerticalPosition.ONE).build();

        PieceCommand pieceCommand = pieceToPieceCommand.convert(piece);

        assertEquals(Long.valueOf(1), pieceCommand.getId());
        assertEquals(PieceColor.WHITE, pieceCommand.getPieceColor());
        assertEquals(PieceType.KING, pieceCommand.getPieceType());
        assertEquals("A1", pieceCommand.getPosition());
        assertEquals(Integer.valueOf(3), pieceCommand.getMoves());
    }

}