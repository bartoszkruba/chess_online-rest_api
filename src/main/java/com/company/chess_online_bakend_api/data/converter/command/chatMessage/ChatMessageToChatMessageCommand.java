/*
 * 7/27/19 3:26 PM. Updated by Bartosz Kruba.
 */

/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.command.chatMessage;

import com.company.chess_online_bakend_api.data.command.ChatMessageCommand;
import com.company.chess_online_bakend_api.data.model.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Slf4j
@Component
public class ChatMessageToChatMessageCommand implements Converter<ChatMessage, ChatMessageCommand> {

    @Nullable
    @Override
    public ChatMessageCommand convert(ChatMessage chatMessage) {

        log.debug("Converting ChatMessage to ChatMessageCommand");

        if (chatMessage == null) {
            return null;
        }

        var chatMessageCommand = ChatMessageCommand.builder()
                .id(chatMessage.getId())
                .message(chatMessage.getMessage()).build();

        if (chatMessage.getRoom() != null) {
            chatMessageCommand.setRoomId(chatMessage.getRoom().getId());
        }

        if (chatMessage.getUser() != null) {
            chatMessageCommand.setUserId(chatMessage.getUser().getId());
            chatMessageCommand.setUsername(chatMessage.getUser().getUsername());
        }

        if (chatMessage.getCreated() != null) {
            ZoneId zoneId = ZoneId.systemDefault();
            chatMessageCommand.setTimestamp(chatMessage.getCreated().atZone(zoneId).toInstant().toEpochMilli());
        }

        return chatMessageCommand;
    }
}
