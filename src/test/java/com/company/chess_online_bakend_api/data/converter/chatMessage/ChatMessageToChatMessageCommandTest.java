/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.chatMessage;

import com.company.chess_online_bakend_api.data.command.ChatMessageCommand;
import com.company.chess_online_bakend_api.data.model.ChatMessage;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ChatMessageToChatMessageCommandTest {

    @InjectMocks
    ChatMessageToChatMessageCommand chatMessageToChatMessageCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void convertNull() {
        assertNull(chatMessageToChatMessageCommand.convert(null));
    }

    @Test
    void convertEmptyObject() {
        ChatMessageCommand chatMessageCommand = chatMessageToChatMessageCommand.convert(ChatMessage.builder().build());

        assertNull(chatMessageCommand.getId());
        assertNull(chatMessageCommand.getMessage());
        assertNull(chatMessageCommand.getRoomId());
        assertNull(chatMessageCommand.getTimestamp());
        assertNull(chatMessageCommand.getUserId());
        assertNull(chatMessageCommand.getUsername());
    }

    @Test
    void convert() {
        final LocalDateTime CREATION_TIME = LocalDateTime.now();

        ChatMessage chatMessage = ChatMessage.builder()
                .id(1L)
                .message("Message")
                .created(CREATION_TIME)
                .room(Room.builder().id(2L).build())
                .user(User.builder().username("username").id(3L).build()).build();


        Long time = CREATION_TIME.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        ChatMessageCommand command = chatMessageToChatMessageCommand.convert(chatMessage);

        assertEquals(Long.valueOf(1), command.getId());
        assertEquals("Message", command.getMessage());
        assertEquals(time, command.getTimestamp());
        assertEquals(Long.valueOf(2), command.getRoomId());
        assertEquals("username", command.getUsername());
        assertEquals(Long.valueOf(3), command.getUserId());
    }
}