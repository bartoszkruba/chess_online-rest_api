/*
 * 8/24/19, 4:55 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service.socket;

import com.company.chess_online_bakend_api.data.converter.notification.chatMessage.ChatMessageToChatNotification;
import com.company.chess_online_bakend_api.data.converter.notification.move.MoveToMoveNotification;
import com.company.chess_online_bakend_api.data.converter.notification.user.UserToUserNotification;
import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import com.company.chess_online_bakend_api.data.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class SocketServiceImplTest {

    private final String USERNAME_ONE = "username one";

    private final String USERNAME_TWO = "username two";

    private final Long gameId = 1L;

    @Mock
    private GameRepository gameRepository;
    @Mock
    private UserToUserNotification userToUserNotification;
    @Mock
    private ChatMessageToChatNotification chatMessageToChatNotification;
    @Mock
    private MoveToMoveNotification moveToMoveNotification;

    @Mock
    private SimpMessageSendingOperations messagingTemplate;

    private SocketServiceImpl socketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        socketService = new SocketServiceImpl(gameRepository, userToUserNotification, chatMessageToChatNotification,
                moveToMoveNotification, messagingTemplate);
    }


    @Test
    void updatePingWhite() {
        var whitePlayer = User.builder().username(USERNAME_ONE).build();
        var blackPlayer = User.builder().username(USERNAME_TWO).build();
        var game = Game.builder()
                .id(gameId)
                .whitePlayer(whitePlayer)
                .blackPlayer(blackPlayer)
                .status(GameStatus.STARTED)
                .build();

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        socketService.updatePlayerPingInGame(gameId, USERNAME_ONE);

        verify(gameRepository, times(1)).findById(gameId);
        verify(gameRepository, times(1)).save(any(Game.class));

        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(userToUserNotification);
        verifyZeroInteractions(chatMessageToChatNotification);
        verifyZeroInteractions(moveToMoveNotification);
        verifyZeroInteractions(messagingTemplate);
    }

    @Test
    void updatePingBlack() {
        var whitePlayer = User.builder().username(USERNAME_ONE).build();
        var blackPlayer = User.builder().username(USERNAME_TWO).build();
        var game = Game.builder()
                .id(gameId)
                .whitePlayer(whitePlayer)
                .blackPlayer(blackPlayer)
                .status(GameStatus.STARTED)
                .build();

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        socketService.updatePlayerPingInGame(gameId, USERNAME_TWO);

        verify(gameRepository, times(1)).findById(gameId);
        verify(gameRepository, times(1)).save(any(Game.class));

        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(userToUserNotification);
        verifyZeroInteractions(chatMessageToChatNotification);
        verifyZeroInteractions(moveToMoveNotification);
        verifyZeroInteractions(messagingTemplate);
    }

    @Test
    void updatePingUsernameNull() {

        socketService.updatePlayerPingInGame(gameId, null);

        verifyZeroInteractions(gameRepository);

        verifyZeroInteractions(userToUserNotification);
        verifyZeroInteractions(chatMessageToChatNotification);
        verifyZeroInteractions(moveToMoveNotification);
        verifyZeroInteractions(messagingTemplate);
    }

    @Test
    void updatePingGameIdNull() {

        socketService.updatePlayerPingInGame(null, USERNAME_TWO);

        verifyZeroInteractions(gameRepository);

        verifyZeroInteractions(userToUserNotification);
        verifyZeroInteractions(chatMessageToChatNotification);
        verifyZeroInteractions(moveToMoveNotification);
        verifyZeroInteractions(messagingTemplate);
    }

    @Test
    void updatePingGameNotFound() {

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        socketService.updatePlayerPingInGame(gameId, USERNAME_TWO);

        verify(gameRepository, times(1)).findById(gameId);

        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(userToUserNotification);
        verifyZeroInteractions(chatMessageToChatNotification);
        verifyZeroInteractions(moveToMoveNotification);
        verifyZeroInteractions(messagingTemplate);
    }

    @Test
    void updatePingPlayerNotInGame() {
        var whitePlayer = User.builder().username(USERNAME_ONE).build();
        var blackPlayer = User.builder().username(USERNAME_TWO).build();
        var game = Game.builder()
                .id(gameId)
                .whitePlayer(whitePlayer)
                .blackPlayer(blackPlayer)
                .status(GameStatus.STARTED)
                .build();

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        socketService.updatePlayerPingInGame(gameId, "Invalid username");

        verify(gameRepository, times(1)).findById(gameId);

        verifyNoMoreInteractions(gameRepository);

        verifyZeroInteractions(userToUserNotification);
        verifyZeroInteractions(chatMessageToChatNotification);
        verifyZeroInteractions(moveToMoveNotification);
        verifyZeroInteractions(messagingTemplate);
    }
}
