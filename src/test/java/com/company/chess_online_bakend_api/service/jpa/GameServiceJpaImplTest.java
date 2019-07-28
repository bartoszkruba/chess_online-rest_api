/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.command.RoomCommand;
import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.converter.command.game.GameCommandToGame;
import com.company.chess_online_bakend_api.data.converter.command.game.GameToGameCommand;
import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.repository.GameRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.exception.*;
import com.company.chess_online_bakend_api.service.socket.SocketService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GameServiceJpaImplTest {

    private User USER_1 = User.builder().id(1L).build();
    private User USER_2 = User.builder().id(2L).build();

    private Room ROOM = Room.builder().id(1L).build();

    private Game GAME_1 = Game.builder()
            .id(1L)
            .status(GameStatus.STARTED)
            .turn(2)
            .room(ROOM)
            .blackPlayer(USER_1)
            .whitePlayer(USER_2).build();

    private Game GAME_2 = Game.builder()
            .id(2L)
            .status(GameStatus.STARTED)
            .turn(2)
            .room(ROOM)
            .blackPlayer(USER_1)
            .whitePlayer(USER_2).build();

    private UserCommand USERCOMMAND1 = UserCommand.builder().id(1L).build();
    private UserCommand USERCOMMAND2 = UserCommand.builder().id(2L).build();

    private RoomCommand ROOMCOMMAND = RoomCommand.builder().id(1L).build();

    private GameCommand GAMECOMMAND1 = GameCommand.builder()
            .id(1L)
            .status(GameStatus.STARTED)
            .turn(2)
            .roomId(ROOMCOMMAND.getId())
            .blackPlayer(USERCOMMAND1)
            .whitePlayer(USERCOMMAND2).build();

    private GameCommand GAMECOMMAND2 = GameCommand.builder()
            .id(2L)
            .status(GameStatus.STARTED)
            .turn(2)
            .roomId(ROOMCOMMAND.getId())
            .blackPlayer(USERCOMMAND1)
            .whitePlayer(USERCOMMAND2).build();

    @Mock
    SocketService socketService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameToGameCommand gameToGameCommand;

    @Mock
    private GameCommandToGame gameCommandToGame;

    @InjectMocks
    private GameServiceJpaImpl gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        when(gameToGameCommand.convert(GAME_1)).thenReturn(GAMECOMMAND1);
        when(gameToGameCommand.convert(GAME_2)).thenReturn(GAMECOMMAND2);

        when(gameCommandToGame.convert(GAMECOMMAND1)).thenReturn(GAME_1);
        when(gameCommandToGame.convert(GAMECOMMAND2)).thenReturn(GAME_2);
    }

    @Test
    void getByRoomId() {
        when(gameRepository.findGameByRoom(ROOM)).thenReturn(Optional.of(GAME_1));

        var gameCommand = gameService.getByRoomId(ROOM.getId());

        assertEquals(GAMECOMMAND1, gameCommand);
        verify(gameToGameCommand, times(1)).convert(GAME_1);
        verifyNoMoreInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verify(gameRepository, times(1)).findGameByRoom(ROOM);
        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(socketService);
    }

    @Test
    void getByRoomIdNotFound() {
        when(gameRepository.findGameByRoom(ROOM)).thenReturn(Optional.empty());

        Assertions.assertThrows(RoomNotFoundException.class, () -> gameService.getByRoomId(ROOM.getId()));

        verify(gameRepository, times(1)).findGameByRoom(ROOM);
        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(gameCommandToGame);
        verifyZeroInteractions(gameToGameCommand);

        verifyZeroInteractions(socketService);
    }

    @Test
    void findById() {
        when(gameRepository.findById(GAME_1.getId())).thenReturn(Optional.of(GAME_1));

        var gameCommand = gameService.findById(GAME_1.getId());

        assertEquals(GAMECOMMAND1, gameCommand);
        verify(gameToGameCommand, times(1)).convert(GAME_1);
        verifyNoMoreInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verify(gameRepository, times(1)).findById(GAME_1.getId());
        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(socketService);
    }

    @Test
    void findByIdNotFound() {
        when(gameRepository.findById(GAME_1.getId())).thenThrow(GameNotFoundException.class);

        Assertions.assertThrows(GameNotFoundException.class, () -> gameService.findById(GAME_1.getId()));

        verifyZeroInteractions(gameCommandToGame);
        verifyZeroInteractions(gameToGameCommand);

        verify(gameRepository, times(1)).findById(GAME_1.getId());
        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(socketService);
    }

    @Test
    void save() {
        when(gameRepository.save(GAME_1)).thenReturn(GAME_1);

        var gameCommand = gameService.save(GAMECOMMAND1);

        verify(gameCommandToGame, times(1)).convert(GAMECOMMAND1);
        verifyNoMoreInteractions(gameCommandToGame);

        verify(gameRepository, times(1)).save(GAME_1);
        verifyNoMoreInteractions(gameRepository);

        verify(gameToGameCommand, times(1)).convert(GAME_1);
        verifyNoMoreInteractions(gameToGameCommand);

        assertEquals(GAMECOMMAND1, gameCommand);

        verifyZeroInteractions(socketService);
    }

    @Test
    void findAll() {
        when(gameRepository.findAll()).thenReturn(Set.of(GAME_1, GAME_2));

        Set<GameCommand> gameCommands = gameService.findAll();

        assertTrue(gameCommands.contains(GAMECOMMAND1));
        assertTrue(gameCommands.contains(GAMECOMMAND2));
        assertEquals(2, gameCommands.size());

        verify(gameToGameCommand, times(1)).convert(GAME_1);
        verify(gameToGameCommand, times(1)).convert(GAME_2);
        verifyNoMoreInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verify(gameRepository, times(1)).findAll();
        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(socketService);
    }

    @Test
    void delete() {
        gameService.delete(GAMECOMMAND1);

        verify(gameCommandToGame, times(1)).convert(GAMECOMMAND1);
        verifyNoMoreInteractions(gameCommandToGame);
        verifyZeroInteractions(gameToGameCommand);

        verify(gameRepository, times(1)).delete(GAME_1);
        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(socketService);
    }

    @Test
    void deleteById() {
        gameService.deleteById(GAME_1.getId());

        verifyZeroInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verify(gameRepository, times(1)).deleteById(GAME_1.getId());
        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(socketService);
    }


    @Test
    void joinGameWhite() {
        String fenNotation = "notation";
        String username = "username";

        User user = User.builder().id(1L).username(username).build();
        UserCommand userCommand = UserCommand.builder().id(1L).build();

        var game = Game.builder()
                .id(1L)
                .status(GameStatus.WAITNG_TO_START)
                .fenNotation(fenNotation)
                .room(ROOM).build();

        var gameWithUser = Game.builder()
                .id(1L)
                .whitePlayer(user)
                .status(GameStatus.WAITNG_TO_START)
                .fenNotation(fenNotation)
                .room(ROOM).build();

        var gameCommandWithUser = GameCommand.builder()
                .id(1L)
                .whitePlayer(userCommand)
                .status(GameStatus.WAITNG_TO_START).build();

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameRepository.save(gameWithUser)).thenReturn(gameWithUser);
        when(gameToGameCommand.convert(gameWithUser)).thenReturn(gameCommandWithUser);

        var gameCommand = gameService.joinGame(PieceColor.WHITE, username, 1L);

        assertEquals(gameCommandWithUser, gameCommand);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(gameRepository, times(1)).findById(1L);
        verify(gameRepository, times(1)).save(gameWithUser);
        verifyNoMoreInteractions(gameRepository);

        verify(gameToGameCommand, times(1)).convert(gameWithUser);
        verifyNoMoreInteractions(gameToGameCommand);

        verifyZeroInteractions(gameCommandToGame);

        verify(socketService, times(1))
                .broadcastJoinGame(user, gameWithUser.getId(), PieceColor.WHITE, fenNotation, ROOM.getId());
        verifyNoMoreInteractions(socketService);
    }

    @Test
    void joinGameBlack() {
        String fenNotation = "notation";
        String username = "username";

        var user = User.builder().id(1L).username(username).build();
        var userCommand = UserCommand.builder().id(1L).build();

        var game = Game.builder()
                .id(1L)
                .status(GameStatus.WAITNG_TO_START)
                .fenNotation(fenNotation)
                .room(ROOM).build();

        var gameWithUser = Game.builder()
                .id(1L)
                .blackPlayer(user)
                .status(GameStatus.WAITNG_TO_START)
                .fenNotation(fenNotation)
                .room(ROOM).build();

        var gameCommandWithUser = GameCommand.builder()
                .id(1L).whitePlayer(userCommand)
                .status(GameStatus.WAITNG_TO_START).build();

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameRepository.save(gameWithUser)).thenReturn(gameWithUser);
        when(gameToGameCommand.convert(gameWithUser)).thenReturn(gameCommandWithUser);

        var gameCommand = gameService.joinGame(PieceColor.BLACK, username, 1L);

        assertEquals(gameCommandWithUser, gameCommand);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(gameRepository, times(1)).findById(1L);
        verify(gameRepository, times(1)).save(gameWithUser);
        verifyNoMoreInteractions(gameRepository);

        verify(gameToGameCommand, times(1)).convert(gameWithUser);
        verifyNoMoreInteractions(gameToGameCommand);

        verifyZeroInteractions(gameCommandToGame);

        verify(socketService, times(1))
                .broadcastJoinGame(user, gameWithUser.getId(), PieceColor.BLACK, fenNotation, ROOM.getId());
        verifyNoMoreInteractions(socketService);
    }

    @Test
    void joinGameWhiteNotFree() {
        String username = "username";

        var user = User.builder().id(1L).username(username).build();
        var gameWithUser = Game.builder()
                .id(1L)
                .whitePlayer(User.builder().id(2L).build())
                .status(GameStatus.WAITNG_TO_START).build();

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(gameWithUser));

        Assertions.assertThrows(PlaceAlreadyTakenException.class, () -> {
            gameService.joinGame(PieceColor.WHITE, username, 1L);
        });

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verifyZeroInteractions(socketService);
    }

    @Test
    void joinGameBlackNotFree() {
        String username = "username";

        var user = User.builder().id(1L).username(username).build();
        var gameWithUser = Game.builder()
                .id(1L)
                .blackPlayer(User.builder().id(2L).build())
                .status(GameStatus.WAITNG_TO_START).build();

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(gameWithUser));

        Assertions.assertThrows(PlaceAlreadyTakenException.class, () -> {
            gameService.joinGame(PieceColor.BLACK, username, 1L);
        });

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verifyZeroInteractions(socketService);
    }

    @Test
    void joinGameInvalidID() {
        String username = "username";

        var user = User.builder().id(1L).username(username).build();

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(GameNotFoundException.class, () -> {
            gameService.joinGame(PieceColor.BLACK, username, 1L);
        });

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verifyZeroInteractions(socketService);
    }

    @Test
    void joinGameUserNotFound() {
        String username = "username";

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            gameService.joinGame(PieceColor.BLACK, username, 1L);
        });

        verifyZeroInteractions(gameRepository);
        verifyZeroInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verifyZeroInteractions(socketService);
    }

    @Test
    void joinGameAlreadyStarted() {
        String username = "username";

        var user = User.builder().id(1L).username(username).build();
        var startedGame = Game.builder()
                .id(1L)
                .status(GameStatus.STARTED).build();

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(startedGame));

        Assertions.assertThrows(GameAlreadyStartedException.class, () -> {
            gameService.joinGame(PieceColor.WHITE, username, 1L);
        });

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verifyZeroInteractions(socketService);
    }

    @Test
    void joinGameAlreadyJoined() {
        String username = "username";

        var user = User.builder().id(1L).username(username).build();
        var joinedGame = Game.builder()
                .id(1L)
                .blackPlayer(user)
                .status(GameStatus.WAITNG_TO_START).build();

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(joinedGame));

        Assertions.assertThrows(AlreadyJoinedException.class, () -> {
            gameService.joinGame(PieceColor.WHITE, username, 1L);
        });

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verifyZeroInteractions(socketService);
    }

    @Test
    void leaveGameWhite() {
        String username = "username";
        String fenNotation = "notation";

        Long gameId = 1L;

        var user = User.builder().id(1L).username(username).build();

        var joinedGame = Game.builder()
                .id(gameId)
                .whitePlayer(user)
                .status(GameStatus.WAITNG_TO_START)
                .fenNotation(fenNotation)
                .room(ROOM).build();

        var gameToSave = Game.builder()
                .id(gameId)
                .status(GameStatus.WAITNG_TO_START)
                .fenNotation(fenNotation)
                .room(ROOM).build();

        var gameWithoutPlayer = GameCommand.builder()
                .id(gameId)
                .status(GameStatus.WAITNG_TO_START).build();

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(joinedGame));
        when(gameRepository.save(gameToSave)).thenReturn(gameToSave);
        when(gameToGameCommand.convert(gameToSave)).thenReturn(gameWithoutPlayer);

        var gameCommand = gameService.leaveGame(username, 1L);

        assertEquals(gameWithoutPlayer, gameCommand);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(gameRepository, times(1)).findById(1L);
        verify(gameRepository, times(1)).save(gameToSave);
        verifyNoMoreInteractions(gameRepository);

        verify(gameToGameCommand, times(1)).convert(gameToSave);
        verifyNoMoreInteractions(gameToGameCommand);

        verifyZeroInteractions(gameCommandToGame);

        verify(socketService, times(1))
                .broadcastLeaveGame(user, gameId, PieceColor.WHITE, fenNotation, ROOM.getId());
        verifyNoMoreInteractions(socketService);
    }

    @Test
    void leaveGameBlack() {
        String username = "username";

        Long gameId = 1L;
        String fenNotation = "notation";

        var user = User.builder().id(1L).username(username).build();

        var joinedGame = Game.builder()
                .id(gameId)
                .blackPlayer(user)
                .status(GameStatus.WAITNG_TO_START)
                .fenNotation(fenNotation)
                .room(ROOM).build();

        var gameToSave = Game.builder()
                .id(gameId)
                .status(GameStatus.WAITNG_TO_START)
                .fenNotation(fenNotation)
                .room(ROOM).build();

        var gameWithoutPlayer = GameCommand.builder()
                .id(gameId)
                .status(GameStatus.WAITNG_TO_START).build();

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(joinedGame));
        when(gameRepository.save(gameToSave)).thenReturn(gameToSave);
        when(gameToGameCommand.convert(gameToSave)).thenReturn(gameWithoutPlayer);

        var gameCommand = gameService.leaveGame(username, 1L);

        assertEquals(gameWithoutPlayer, gameCommand);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(gameRepository, times(1)).findById(1L);
        verify(gameRepository, times(1)).save(gameToSave);
        verifyNoMoreInteractions(gameRepository);

        verify(gameToGameCommand, times(1)).convert(gameToSave);
        verifyNoMoreInteractions(gameToGameCommand);

        verifyZeroInteractions(gameCommandToGame);

        verify(socketService, times(1))
                .broadcastLeaveGame(user, gameId, PieceColor.BLACK, fenNotation, ROOM.getId());
        verifyNoMoreInteractions(socketService);
    }

    @Test
    void leaveGameUsernameNotFound() {
        String username = "username";

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            gameService.leaveGame(username, 1L);
        });

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verifyZeroInteractions(gameRepository);
        verifyZeroInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verifyZeroInteractions(socketService);
    }

    @Test
    void leaveGameGameNotFound() {
        String username = "username";

        var user = User.builder().id(1L).username(username).build();

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(GameNotFoundException.class, () -> {
            gameService.leaveGame(username, 1L);
        });

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verifyZeroInteractions(socketService);
    }

    @Test
    void leaveGameHasNotJoined() {
        String username = "username";

        var user = User.builder().id(1L).username(username).build();

        var gameWithoutPlayer = Game.builder()
                .id(1L)
                .status(GameStatus.WAITNG_TO_START).build();

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(gameWithoutPlayer));

        Assertions.assertThrows(UserNotFoundException.class, () -> gameService.leaveGame(username, 1L));

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(gameToGameCommand);

        verifyZeroInteractions(gameCommandToGame);

        verifyZeroInteractions(socketService);
    }

    @Test
    @Disabled
    void leaveGameAlreadyBegin() {
        String username = "username";

        var user = User.builder().id(1L).username(username).build();

        var joinedGame = Game.builder()
                .id(1L)
                .blackPlayer(user)
                .status(GameStatus.STARTED)
                .room(ROOM).build();

        var gameToSave = Game.builder()
                .id(1L)
                .status(GameStatus.STOPPED)
                .room(ROOM).build();

        var gameWithoutPlayer = GameCommand.builder()
                .id(1L)
                .status(GameStatus.STOPPED).build();

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(joinedGame));
        when(gameRepository.save(gameToSave)).thenReturn(gameToSave);
        when(gameToGameCommand.convert(gameToSave)).thenReturn(gameWithoutPlayer);

        var gameCommand = gameService.leaveGame(username, 1L);

        assertEquals(gameWithoutPlayer, gameCommand);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(gameRepository, times(1)).findById(1L);
        verify(gameRepository, times(1)).save(gameToSave);
        verifyNoMoreInteractions(gameRepository);

        verify(gameToGameCommand, times(1)).convert(gameToSave);
        verifyNoMoreInteractions(gameToGameCommand);

        verifyZeroInteractions(gameCommandToGame);

        // TODO: 2019-07-28 checkout for creation of new game
        // TODO: 2019-07-28 checkout for "game over" notification
    }
}