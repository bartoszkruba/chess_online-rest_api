/*
 * 7/27/19 3:37 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.notification.chatMessage;

import com.company.chess_online_bakend_api.data.model.ChatMessage;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.notification.ChatMessageNotification;
import com.company.chess_online_bakend_api.data.notification.enums.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ChatMessageToChatNotificationTest {

    @InjectMocks
    ChatMessageToChatNotification chatMessageToChatNotification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void convertNull() {
        assertNull(chatMessageToChatNotification.convert(null));
    }

    @Test
    void convertEmptyObject() {
        ChatMessageNotification notification = chatMessageToChatNotification.convert(ChatMessage.builder().build());

        assertNull(notification.getMessage());
        assertNull(notification.getRoomId());
        assertNull(notification.getTimestamp());
        assertNull(notification.getUserId());
        assertNull(notification.getUsername());
        assertEquals(NotificationType.CHAT_MESSAGE, notification.getNotificationType());
    }

    @Test
    void convert() {
        String username = "username";
        Long userId = 1L;
        Long roomId = 1L;

        String message = "message";

        var created = LocalDateTime.now();
        var user = User.builder().username(username).id(userId).build();
        var room = Room.builder().id(roomId).build();

        var chatMessage = ChatMessage.builder()
                .user(user)
                .room(room)
                .message(message)
                .created(created).build();

        ChatMessageNotification notification = chatMessageToChatNotification.convert(chatMessage);

        assertEquals(message, notification.getMessage());
        assertEquals(userId, notification.getUserId());
        assertEquals(username, notification.getUsername());
        assertEquals(roomId, notification.getUserId());

        Long time = created.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        assertEquals(time, notification.getTimestamp());
    }
}