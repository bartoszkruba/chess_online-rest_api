package com.company.chess_online_bakend_api.data.converter.chatMessage;

import com.company.chess_online_bakend_api.data.command.ChatMessageCommand;
import com.company.chess_online_bakend_api.data.model.ChatMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ChatMessageCommandToChatMessageTest {

    @InjectMocks
    ChatMessageCommandToChatMessage chatMessageCommandToChatMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void convertNull() {
        assertNull(chatMessageCommandToChatMessage.convert(null));
    }

    @Test
    void convertEmptyObject() {
        ChatMessage chatMessage = chatMessageCommandToChatMessage.convert(ChatMessageCommand.builder().build());

        assertNull(chatMessage.getMessage());
        assertNull(chatMessage.getUser());
        assertNull(chatMessage.getRoom());
        assertNull(chatMessage.getCreated());
        assertNull(chatMessage.getId());
    }

    @Test
    void convert() {
        ChatMessageCommand command = ChatMessageCommand.builder().message("Message").build();

        ChatMessage chatMessage = chatMessageCommandToChatMessage.convert(command);

        assertEquals("Message", chatMessage.getMessage());
    }
}