package com.company.chess_online_bakend_api.data.converter.game;

import com.company.chess_online_bakend_api.data.command.BoardCommand;
import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.converter.board.BoardCommandToBoard;
import com.company.chess_online_bakend_api.data.converter.user.UserCommandToUser;
import com.company.chess_online_bakend_api.data.model.Board;
import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class GameCommandToGameTest {

    private final Long GAME_ID = 1L;
    private final Integer TURN = 1;

    private final User PLAYER_1 = User.builder()
            .email("player1@email.com")
            .firstName("John")
            .lastName("Doe")
            .username("john69")
            .id(3L).build();

    private final User PLAYER_2 = User.builder()
            .email("player2@email.com")
            .firstName("Michael")
            .lastName("Jackson")
            .username("mike69")
            .id(4L).build();

    private final Board BOARD = Board.builder()
            .id(9L).build();

    @Mock
    private UserCommandToUser userCommandToUser;

    @Mock
    private BoardCommandToBoard boardCommandToBoard;

    @InjectMocks
    private GameCommandToGame gameCommandToGame;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testConvertNullObject() {
        assertNull(gameCommandToGame.convert(null));

        verifyZeroInteractions(userCommandToUser);
        verifyZeroInteractions(boardCommandToBoard);
    }

    @Test
    void testConvertEmptyObject() {
        Game convertedGame = gameCommandToGame
                .convert(GameCommand.builder().build());

        assertNull(convertedGame.getBlackPlayer());
        assertNull(convertedGame.getWhitePlayer());
        assertNull(convertedGame.getRoom());
        assertNull(convertedGame.getStatus());
        assertNull(convertedGame.getTurn());
        assertNull(convertedGame.getId());
        assertNull(convertedGame.getBoard());
        assertNull(convertedGame.getFenNotation());
        assertNull(convertedGame.getIsKingAttacked());
        assertNull(convertedGame.getIsCheckmate());
        assertNull(convertedGame.getIsDraw());

        verifyZeroInteractions(userCommandToUser);
        verifyZeroInteractions(boardCommandToBoard);
    }

    @Test
    void testConvert() {
        UserCommand whitePlayer = UserCommand.builder().id(3L).build();
        UserCommand blackPlayer = UserCommand.builder().id(4L).build();

        BoardCommand boardCommand = BoardCommand.builder().id(5L).build();

        when(userCommandToUser.convert(whitePlayer)).thenReturn(PLAYER_1);
        when((userCommandToUser.convert(blackPlayer))).thenReturn(PLAYER_2);
        when(boardCommandToBoard.convert(boardCommand)).thenReturn(BOARD);

        GameCommand game = GameCommand.builder().id(GAME_ID)
                .whitePlayer(whitePlayer)
                .blackPlayer(blackPlayer)
                .fenNotation("AAA")
                .status(GameStatus.STARTED)
                .isKingAttacked(true)
                .isCheckmate(true)
                .isDraw(true)
                .turn(TURN).build();

        game.setBoard(boardCommand);

        Game convertedGame = gameCommandToGame.convert(game);

        assertEquals(PLAYER_1, convertedGame.getWhitePlayer());
        assertEquals(PLAYER_2, convertedGame.getBlackPlayer());
        assertNull(convertedGame.getRoom());
        assertEquals(GAME_ID, convertedGame.getId());
        assertEquals(GameStatus.STARTED, convertedGame.getStatus());
        assertEquals(TURN, convertedGame.getTurn());
        assertEquals(BOARD, convertedGame.getBoard());
        assertEquals("AAA", convertedGame.getFenNotation());
        assertEquals(true, convertedGame.getIsKingAttacked());
        assertEquals(true, convertedGame.getIsCheckmate());
        assertEquals(true, convertedGame.getIsDraw());

        verify(userCommandToUser, times(1)).convert(whitePlayer);
        verify(userCommandToUser, times(1)).convert(blackPlayer);
        verify(boardCommandToBoard, times(1)).convert(boardCommand);

        verifyNoMoreInteractions(userCommandToUser, boardCommandToBoard);
    }
}