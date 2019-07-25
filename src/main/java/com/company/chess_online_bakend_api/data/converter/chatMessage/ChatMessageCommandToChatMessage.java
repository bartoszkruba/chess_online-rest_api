package com.company.chess_online_bakend_api.data.converter.chatMessage;

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

        ChatMessage chatMessage = ChatMessage.builder()
                .message(chatMessageCommand.getMessage()).build();

        return chatMessage;
    }
}
