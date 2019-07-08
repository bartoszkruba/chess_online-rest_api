package com.company.chess_online_bakend_api.data.converter;

import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.GameStatus;
import com.company.chess_online_bakend_api.data.model.User;
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

    @Mock
    private UserCommandToUser userCommandToUser;

    @InjectMocks
    private GameCommandToGame gameCommandToGame;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testConvertNullObject() {
        assertNull(gameCommandToGame.convert(null));

        verify(userCommandToUser, times(0)).convert(any());
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

        verify(userCommandToUser, times(0)).convert(any());
    }

    @Test
    void testConvert() {
        UserCommand whitePlayer = UserCommand.builder().id(3L).build();
        UserCommand blackPlayer = UserCommand.builder().id(4L).build();

        when(userCommandToUser.convert(whitePlayer)).thenReturn(PLAYER_1);
        when((userCommandToUser.convert(blackPlayer))).thenReturn(PLAYER_2);

        GameCommand game = GameCommand.builder().id(GAME_ID)
                .whitePlayer(whitePlayer)
                .blackPlayer(blackPlayer)
                .status(GameStatus.STARTED)
                .turn(TURN).build();

        Game convertedGame = gameCommandToGame.convert(game);

        assertEquals(PLAYER_1, convertedGame.getWhitePlayer());
        assertEquals(PLAYER_2, convertedGame.getBlackPlayer());
        assertNull(convertedGame.getRoom());
        assertEquals(GAME_ID, convertedGame.getId());
        assertEquals(GameStatus.STARTED, convertedGame.getStatus());
        assertEquals(TURN, convertedGame.getTurn());

        verify(userCommandToUser, times(1)).convert(whitePlayer);
        verify(userCommandToUser, times(1)).convert(blackPlayer);
    }
}