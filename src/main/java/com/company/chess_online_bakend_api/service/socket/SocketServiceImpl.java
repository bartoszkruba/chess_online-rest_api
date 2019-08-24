/*
 * 7/27/19 3:53 PM. Updated by Bartosz Kruba.
 */

/*
 * 7/27/19 3:53 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service.socket;

import com.company.chess_online_bakend_api.data.converter.notification.chatMessage.ChatMessageToChatNotification;
import com.company.chess_online_bakend_api.data.converter.notification.move.MoveToMoveNotification;
import com.company.chess_online_bakend_api.data.converter.notification.user.UserToUserNotification;
import com.company.chess_online_bakend_api.data.model.ChatMessage;
import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.Move;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.notification.GameOverNotification;
import com.company.chess_online_bakend_api.data.notification.JoinGameNotification;
import com.company.chess_online_bakend_api.data.notification.LeaveGameNotification;
import com.company.chess_online_bakend_api.data.notification.enums.GameOverCause;
import com.company.chess_online_bakend_api.data.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class SocketServiceImpl implements SocketService {

    private final GameRepository gameRepository;

    private final UserToUserNotification userToUserNotification;
    private final ChatMessageToChatNotification chatMessageToChatNotification;
    private final MoveToMoveNotification moveToMoveNotification;

    private final SimpMessageSendingOperations messagingTemplate;

    public SocketServiceImpl(GameRepository gameRepository, UserToUserNotification userToUserNotification,
                             ChatMessageToChatNotification chatMessageToChatNotification,
                             MoveToMoveNotification moveToMoveNotification,
                             SimpMessageSendingOperations messagingTemplate) {
        this.gameRepository = gameRepository;
        this.userToUserNotification = userToUserNotification;
        this.chatMessageToChatNotification = chatMessageToChatNotification;
        this.moveToMoveNotification = moveToMoveNotification;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @Async
    public void broadcastChatMessage(ChatMessage chatMessage) {
        // '/' cannot be used in AMQP specifications. Therefore im using '.'
        String channel = "/topic/room." + chatMessage.getRoom().getId();

        log.debug("Broadcasting ChatMessage to " + channel);

        messagingTemplate.convertAndSend(channel, chatMessageToChatNotification.convert(chatMessage));
    }

    @Override
    @Async
    public void broadcastJoinGame(User user, Long gameId, PieceColor color, String fenNotation, Long roomId) {
        String channel = "/topic/room." + roomId;

        log.debug("Broadcasting JoinGame to " + channel);

        if (gameId == null) {
            log.error("Trying to send JoinGameNotification with null gameId");
            return;
        }

        var joinGameNotification = JoinGameNotification.builder()
                .user(userToUserNotification.convert(user))
                .gameId(gameId)
                .color(color)
                .fenNotation(fenNotation).build();

        log.debug(joinGameNotification.toString());

        messagingTemplate.convertAndSend(channel, joinGameNotification);
    }

    @Override
    @Async
    public void broadcastLeaveGame(User user, Long gameId, PieceColor color, String fenNotation, Long roomId) {
        String channel = "/topic/room." + roomId;

        log.debug("Broadcasting LeaveGame to room " + channel);

        if (roomId == null) {
            log.error("Trying to send LeaveGameNotification with null roomId");
        }

        var leaveGameNotification = LeaveGameNotification.builder()
                .user(userToUserNotification.convert(user))
                .gameId(gameId)
                .color(color)
                .fenNotation(fenNotation).build();

        messagingTemplate.convertAndSend(channel, leaveGameNotification);
    }

    @Override
    @Async
    public void broadcastMove(Move move, String fenNotation, Long gameId, Long roomId) {
        String channel = "/topic/room." + roomId;

        log.debug("Broadcasting Move to room " + channel);

        if (roomId == null) {
            log.error("Trying to send MoveNotification with null roomId");
            return;
        }

        if (move == null) {
            log.error("Trying to send MoveNotification with null move");
            return;
        }

        var moveNotification = moveToMoveNotification.convert(move);
        moveNotification.setFenNotation(fenNotation);
        moveNotification.setGameId(gameId);

        messagingTemplate.convertAndSend(channel, moveNotification);
    }

    @Override
    @Async
    public void broadcastGameOverWithDraw(String fenNotation, Long gameId, Long roomId) {
        String channel = "/topic/room." + roomId;

        log.debug("Broadcasting GameOver with draw to room " + channel);

        if (roomId == null) {
            log.error("Trying to broadcast GameOver with null roomId");

            return;
        }

        var gameOverNotification = GameOverNotification.builder()
                .gameOverCause(GameOverCause.DRAW)
                .gameId(gameId)
                .fenNotation(fenNotation)
                .build();

        messagingTemplate.convertAndSend(channel, gameOverNotification);
    }

    @Override
    @Async
    public void broadcastGameOverWithCheckmate(User winner, PieceColor winnerColor, String fenNotation, Long gameId,
                                               Long roomId) {
        String channel = "/topic/room." + roomId;

        log.debug("Broadcasting GameOver with draw to room " + channel);

        if (roomId == null) {
            log.error("Trying to broadcast GameOver with null roomId");
            return;
        }

        var gameOverNotification = GameOverNotification.builder()
                .gameOverCause(GameOverCause.CHECKMATE)
                .gameId(gameId)
                .fenNotation(fenNotation)
                .winner(userToUserNotification.convert(winner))
                .winnerColor(winnerColor)
                .build();

        messagingTemplate.convertAndSend(channel, gameOverNotification);
    }

    @Override
    @Async
    public void broadcastGameOverWithPlayerLeft(User winner, PieceColor winnerColor, String fenNotation, Long gameId,
                                                Long roomId) {
        String channel = "/topic/room." + roomId;

        log.debug("Broadcasting GameOver with draw to room " + channel);

        if (roomId == null) {
            log.error("Trying to broadcast GameOver with null roomId");
            return;
        }

        var gameOverNotification = GameOverNotification.builder()
                .gameOverCause(GameOverCause.PLAYER_LEFT)
                .gameId(gameId)
                .fenNotation(fenNotation)
                .winner(userToUserNotification.convert(winner))
                .winnerColor(winnerColor)
                .build();

        messagingTemplate.convertAndSend(channel, gameOverNotification);
    }

    @Override
    @Async
    public void updatePlayerPingInGame(Long gameId, String username) {
        if (username == null) return;
        Optional<Game> gameOptional = gameRepository.findById(gameId);

        if (gameOptional.isEmpty()) return;
        var game = gameOptional.get();

        if (game.getStatus() != GameStatus.STARTED) return;

        if (game.getWhitePing() != null && username.equals(game.getWhitePlayer().getUsername())) {
            game.setWhitePing(LocalDateTime.now());
            gameRepository.save(game);
        } else if (game.getBlackPlayer() != null && username.equals(game.getBlackPlayer().getUsername())) {
            game.setBlackPing(LocalDateTime.now());
            gameRepository.save(game);
        }
    }
}
