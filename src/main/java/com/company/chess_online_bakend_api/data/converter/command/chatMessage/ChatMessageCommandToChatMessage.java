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

@Slf4j
@Component
public class ChatMessageCommandToChatMessage implements Converter<ChatMessageCommand, ChatMessage> {

    @Nullable
    @Override
    public ChatMessage convert(ChatMessageCommand chatMessageCommand) {
        log.debug("Converting ChatMessageCommand to ChatMessage");

        if (chatMessageCommand == null) {
            return null;
        }

        return ChatMessage.builder()
                .message(chatMessageCommand.getMessage()).build();
    }
}
