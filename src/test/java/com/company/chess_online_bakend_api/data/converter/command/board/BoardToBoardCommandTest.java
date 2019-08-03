/*
 * 8/3/19, 3:16 PM. Updated by Bartosz Kruba.
 */

/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.command.board;

import com.company.chess_online_bakend_api.data.command.BoardCommand;
import com.company.chess_online_bakend_api.data.command.PieceCommand;
import com.company.chess_online_bakend_api.data.converter.command.piece.PieceToPieceCommand;
import com.company.chess_online_bakend_api.data.model.Board;
import com.company.chess_online_bakend_api.data.model.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoardToBoardCommandTest {

    private final Long BOARD_ID = 1L;

    @Mock
    PieceToPieceCommand pieceToPieceCommand;

    @InjectMocks
    BoardToBoardCommand boardToBoardCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testConvertNull() {
        assertNull(boardToBoardCommand.convert(null));

        verifyZeroInteractions(pieceToPieceCommand);
    }

    @Test
    void testConvertEmptyObject() {
        Board board = Board.builder().build();
        board.setPieces(null);

        BoardCommand boardCommand = boardToBoardCommand.convert(board);

        assertNull(boardCommand.getId());
        assertNull(boardCommand.getPieces());

        verifyZeroInteractions(pieceToPieceCommand);
    }

    @Test
    void testConvert() {
        Board board = Board.builder().id(BOARD_ID).build();

        Piece piece1 = Piece.builder().id(2L).build();
        Piece piece2 = Piece.builder().id(3L).build();

        board.setPieces(new HashSet<>());
        board.getPieces().add(piece1);
        board.getPieces().add(piece2);

        PieceCommand pieceCommand1 = PieceCommand.builder().id(2L).build();
        PieceCommand pieceCommand2 = PieceCommand.builder().id(3L).build();

        when(pieceToPieceCommand.convert(piece1)).thenReturn(pieceCommand1);
        when(pieceToPieceCommand.convert(piece2)).thenReturn(pieceCommand2);

        BoardCommand boardCommand = boardToBoardCommand.convert(board);

        assertEquals(BOARD_ID, boardCommand.getId());
        assertEquals(2, boardCommand.getPieces().size());
        assertTrue(boardCommand.getPieces().contains(pieceCommand1));
        assertTrue(boardCommand.getPieces().contains(pieceCommand2));

        verify(pieceToPieceCommand, times(1)).convert(piece1);
        verify(pieceToPieceCommand, times(1)).convert(piece2);
    }
}