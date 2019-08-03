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
import com.company.chess_online_bakend_api.data.model.Move;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.notification.JoinGameNotification;
import com.company.chess_online_bakend_api.data.notification.LeaveGameNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SocketServiceImpl implements SocketService {

    private final UserToUserNotification userToUserNotification;
    private final ChatMessageToChatNotification chatMessageToChatNotification;
    private final MoveToMoveNotification moveToMoveNotification;

    private final SimpMessageSendingOperations messagingTemplate;

    public SocketServiceImpl(UserToUserNotification userToUserNotification,
                             ChatMessageToChatNotification chatMessageToChatNotification,
                             MoveToMoveNotification moveToMoveNotification,
                             SimpMessageSendingOperations messagingTemplate) {
        this.userToUserNotification = userToUserNotification;
        this.chatMessageToChatNotification = chatMessageToChatNotification;
        this.moveToMoveNotification = moveToMoveNotification;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @Async
    public void broadcastChatMessage(ChatMessage chatMessage) {
        String channel = "/topic/room/" + chatMessage.getRoom().getId();

        log.debug("Broadcasting ChatMessage to " + channel);

        messagingTemplate.convertAndSend(channel, chatMessageToChatNotification.convert(chatMessage));
    }

    @Override
    @Async
    public void broadcastJoinGame(User user, Long gameId, PieceColor color, String fenNotation, Long roomId) {
        String channel = "/topic/room/" + roomId;

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
        String channel = "/topic/room/" + roomId;

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
    public void broadcastMove(Move move, Long roomId) {
        String channel = "/topic/room/" + roomId;

        log.debug("Broadcasting Move to room " + channel);

        if (roomId == null) {
            log.error("Trying to send Move Notification with null roomId");
        }

        var moveNotification = moveToMoveNotification.convert(move);

        messagingTemplate.convertAndSend(channel, moveNotification);
    }
}
