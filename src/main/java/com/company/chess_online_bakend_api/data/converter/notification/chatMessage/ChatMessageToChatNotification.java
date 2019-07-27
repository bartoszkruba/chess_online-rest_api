/*
 * 7/27/19 3:30 PM. Updated by Bartosz Kruba.
 */

/*
 * 7/27/19 3:28 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.notification.chatMessage;

import com.company.chess_online_bakend_api.data.model.ChatMessage;
import com.company.chess_online_bakend_api.data.notification.ChatMessageNotification;
import com.company.chess_online_bakend_api.data.notification.enums.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Slf4j
@Component
public class ChatMessageToChatNotification implements Converter<ChatMessage, ChatMessageNotification> {

    @Override
    @Nullable
    public ChatMessageNotification convert(ChatMessage chatMessage) {
        log.debug("Converting ChatMessage to ChatMessageNotification");

        if (chatMessage == null) {
            return null;
        }

        var chatMessageNotification = ChatMessageNotification.builder()
                .notificationType(NotificationType.CHAT_MESSAGE)
                .message(chatMessage.getMessage()).build();

        if (chatMessage.getRoom() != null) {
            chatMessageNotification.setRoomId(chatMessage.getRoom().getId());
        }

        if (chatMessage.getUser() != null) {
            chatMessageNotification.setUserId(chatMessage.getUser().getId());
            chatMessageNotification.setUsername(chatMessage.getUser().getUsername());
        }

        if (chatMessage.getCreated() != null) {
            ZoneId zoneId = ZoneId.systemDefault();
            chatMessageNotification.setTimestamp(chatMessage
                    .getCreated()
                    .atZone(zoneId)
                    .toInstant()
                    .toEpochMilli());
        }

        return chatMessageNotification;
    }
}
