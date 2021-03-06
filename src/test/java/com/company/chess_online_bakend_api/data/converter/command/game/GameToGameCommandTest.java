/*
 * 8/3/19, 3:17 PM. Updated by Bartosz Kruba.
 */

/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.command.game;

import com.company.chess_online_bakend_api.data.command.BoardCommand;
import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.command.PieceCommand;
import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.converter.command.board.BoardToBoardCommand;
import com.company.chess_online_bakend_api.data.converter.command.user.UserToUserCommand;
import com.company.chess_online_bakend_api.data.model.Board;
import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class GameToGameCommandTest {

    private final Long GAME_ID = 1L;
    private final Integer TURN = 1;

    private final Room ROOM = Room.builder().id(2L).name("Room Name").build();

    private final UserCommand PLAYER_1_COMMAND = UserCommand.builder()
            .email("player1@email.com")
            .firstName("John")
            .lastName("Doe")
            .username("john69")
            .id(3L).build();

    private final UserCommand PLAYER_2_COMMAND = UserCommand.builder()
            .email("player2@email.com")
            .firstName("Michael")
            .lastName("Jackson")
            .username("mike69")
            .id(4L).build();

    private final BoardCommand BOARD_COMMAND = BoardCommand.builder()
            .id(6L)
            .pieces(Set.of(PieceCommand.builder().id(7L).build(),
                    PieceCommand.builder().id(8L).build())).build();

    @Mock
    UserToUserCommand userToUserCommand;

    @Mock
    BoardToBoardCommand boardToBoardCommand;

    @InjectMocks
    GameToGameCommand gameToGameCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testConvertNullObject() {
        assertNull(gameToGameCommand.convert(null));

        verifyZeroInteractions(userToUserCommand);
        verifyZeroInteractions(boardToBoardCommand);
    }

    @Test
    void testConvertEmptyObject() {
        GameCommand convertedGame = gameToGameCommand
                .convert(Game.builder().build());

        assertNull(convertedGame.getBlackPlayer());
        assertNull(convertedGame.getWhitePlayer());
        assertNull(convertedGame.getRoomId());
        assertNull(convertedGame.getStatus());
        assertNull(convertedGame.getTurn());
        assertNull(convertedGame.getId());
        assertNull(convertedGame.getBoard());
        assertNull(convertedGame.getIsKingAttacked());
        assertNull(convertedGame.getIsCheckmate());
        assertNull(convertedGame.getIsDraw());

        verifyZeroInteractions(userToUserCommand);
        verifyZeroInteractions(boardToBoardCommand);
    }

    @Test
    void testConvert() {
        User whitePlayer = User.builder().id(3L).build();
        User blackPlayer = User.builder().id(4L).build();
        Board board = Board.builder().id(7L).build();

        when(userToUserCommand.convert(whitePlayer)).thenReturn(PLAYER_1_COMMAND);
        when(userToUserCommand.convert(blackPlayer)).thenReturn(PLAYER_2_COMMAND);
        when(boardToBoardCommand.convert(board)).thenReturn(BOARD_COMMAND);

        Game game = Game.builder()
                .whitePlayer(whitePlayer)
                .blackPlayer(blackPlayer)
                .room(ROOM)
                .id(GAME_ID)
                .status(GameStatus.STARTED)
                .turn(TURN)
                .isKingAttacked(true)
                .isCheckmate(true)
                .isDraw(true)
                .board(board).build();

        GameCommand convertedGame = gameToGameCommand.convert(game);

        assertEquals(PLAYER_1_COMMAND, convertedGame.getWhitePlayer());
        assertEquals(PLAYER_2_COMMAND, convertedGame.getBlackPlayer());
        assertEquals(ROOM.getId(), convertedGame.getRoomId());
        assertEquals(GAME_ID, convertedGame.getId());
        assertEquals(GameStatus.STARTED, convertedGame.getStatus());
        assertEquals(TURN, convertedGame.getTurn());
        assertEquals(BOARD_COMMAND, convertedGame.getBoard());
        assertEquals(true, convertedGame.getIsKingAttacked());
        assertEquals(true, convertedGame.getIsCheckmate());
        assertEquals(true, convertedGame.getIsDraw());

        verify(userToUserCommand, times(1)).convert(whitePlayer);
        verify(userToUserCommand, times(1)).convert(blackPlayer);
        verify(boardToBoardCommand, times(1)).convert(board);
    }
}