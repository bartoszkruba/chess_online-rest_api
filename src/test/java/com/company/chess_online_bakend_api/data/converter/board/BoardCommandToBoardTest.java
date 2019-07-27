/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.board;

import com.company.chess_online_bakend_api.data.command.BoardCommand;
import com.company.chess_online_bakend_api.data.command.PieceCommand;
import com.company.chess_online_bakend_api.data.converter.command.board.BoardCommandToBoard;
import com.company.chess_online_bakend_api.data.converter.command.piece.PieceCommandToPiece;
import com.company.chess_online_bakend_api.data.model.Board;
import com.company.chess_online_bakend_api.data.model.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoardCommandToBoardTest {

    private final Long BOARD_ID = 1L;

    @Mock
    PieceCommandToPiece pieceCommandToPiece;

    @InjectMocks
    BoardCommandToBoard boardCommandToBoard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testConvertNull() {
        assertNull(boardCommandToBoard.convert(null));
        verifyZeroInteractions(pieceCommandToPiece);
    }

    @Test
    void testConvertEmptyObject() {
        BoardCommand boardCommand = BoardCommand.builder().build();
        boardCommand.setPieces(null);

        Board board = boardCommandToBoard.convert(boardCommand);

        assertNull(board.getId());
        assertNull(board.getPieces());

        verifyZeroInteractions(pieceCommandToPiece);
    }

    @Test
    void testConvert() {
        BoardCommand boardCommand = BoardCommand.builder().id(BOARD_ID).build();

        PieceCommand pieceCommand1 = PieceCommand.builder().id(2L).build();
        PieceCommand pieceCommand2 = PieceCommand.builder().id(3L).build();

        boardCommand.setPieces(Set.of(pieceCommand1, pieceCommand2));

        Piece piece1 = Piece.builder().id(2L).build();
        Piece piece2 = Piece.builder().id(3L).build();

        when(pieceCommandToPiece.convert(pieceCommand1)).thenReturn(piece1);
        when(pieceCommandToPiece.convert(pieceCommand2)).thenReturn(piece2);

        Board board = boardCommandToBoard.convert(boardCommand);

        assertEquals(BOARD_ID, board.getId());
        assertEquals(2, board.getPieces().size());
        assertTrue(board.getPieces().contains(piece1));
        assertTrue(board.getPieces().contains(piece2));

        verify(pieceCommandToPiece, times(1)).convert(pieceCommand1);
        verify(pieceCommandToPiece, times(1)).convert(pieceCommand2);
        verifyNoMoreInteractions(pieceCommandToPiece);
    }
}