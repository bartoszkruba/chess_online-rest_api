/*
 * 7/26/19 7:51 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.ChatMessageCommand;
import com.company.chess_online_bakend_api.data.converter.chatMessage.ChatMessageToChatMessageCommand;
import com.company.chess_online_bakend_api.data.model.ChatMessage;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.ChatMessageRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.exception.RoomNotFoundException;
import com.company.chess_online_bakend_api.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class ChatMessageServiceJpaImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoomRepository roomRepository;

    @Mock
    ChatMessageRepository chatMessageRepository;

    @Mock
    ChatMessageToChatMessageCommand chatMessageToChatMessageCommand;

    @InjectMocks
    ChatMessageServiceJpaImpl chatMessageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getMessageCountForRoom() {
        Room room = Room.builder().id(1L).build();

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        when(chatMessageRepository.countByRoom(room)).thenReturn(3L);

        Long count = chatMessageService.getMessageCountForRoom(1L);

        assertEquals(Long.valueOf(3L), count);

        verify(roomRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(roomRepository);

        verify(chatMessageRepository, times(1)).countByRoom(room);
        verifyNoMoreInteractions(chatMessageRepository);

        verifyZeroInteractions(chatMessageToChatMessageCommand);
    }

    @Test
    void getMessageCountForRoomInvalidId() {
        Room room = Room.builder().id(1L).build();

        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        when(chatMessageRepository.countByRoom(room)).thenReturn(3L);

        assertThrows(RoomNotFoundException.class, () -> {
            chatMessageService.getMessageCountForRoom(1L);
        });

        verify(roomRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(roomRepository);

        verifyZeroInteractions(chatMessageRepository);

        verifyZeroInteractions(chatMessageToChatMessageCommand);
    }

    @Test
    void getMessagePageForRoom() {
        ChatMessage chatMessage1 = ChatMessage.builder().id(1L).build();
        ChatMessage chatMessage2 = ChatMessage.builder().id(2L).build();

        List<ChatMessage> messages = Arrays.asList(chatMessage1, chatMessage2);
        Page messagesPage = new PageImpl(messages);

        Room room = Room.builder().id(1L).build();

//        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("created").ascending());

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(chatMessageRepository.findByRoom(any(), any())).thenReturn(messagesPage);

        when(chatMessageToChatMessageCommand.convert(chatMessage1))
                .thenReturn(ChatMessageCommand.builder().id(1L).build());
        when(chatMessageToChatMessageCommand.convert(chatMessage2))
                .thenReturn(ChatMessageCommand.builder().id(2L).build());

        List<ChatMessageCommand> messageCommands = chatMessageService.getMessagePageForRoom(1L, 0);

        assertEquals(2, messageCommands.size());
        assertEquals(Long.valueOf(1), messageCommands.get(0).getId());
        assertEquals(Long.valueOf(2), messageCommands.get(1).getId());

        verify(roomRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(roomRepository);

        verify(chatMessageRepository, times(1)).findByRoom(any(), any());
        verifyNoMoreInteractions(chatMessageRepository);

        verify(chatMessageToChatMessageCommand, times(1)).convert(chatMessage1);
        verify(chatMessageToChatMessageCommand, times(1)).convert(chatMessage2);
        verifyNoMoreInteractions(chatMessageToChatMessageCommand);
    }

    @Test
    void getMessagePageForRoomInvalidId() {

        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> {
            chatMessageService.getMessagePageForRoom(1L, 0);
        });

        verify(roomRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(roomRepository);

        verifyZeroInteractions(chatMessageRepository);

        verifyZeroInteractions(chatMessageToChatMessageCommand);
    }

    @Test
    void createNewMessageInvalidUsername() {
        String username = "username";
        String message = "message";
        Long roomId = 1L;

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> chatMessageService.createNewMessage(message, username, roomId));

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verifyZeroInteractions(roomRepository);

        verifyZeroInteractions(chatMessageRepository);

        verifyZeroInteractions(chatMessageToChatMessageCommand);
    }

    @Test
    void createNewMessageInvalidRoomId() {
        String username = "username";
        String message = "message";
        Long roomId = 1L;

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(User.builder().build()));
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> chatMessageService.createNewMessage(message, username, roomId));

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(roomRepository, times(1)).findById(roomId);
        verifyNoMoreInteractions(roomRepository);

        verifyZeroInteractions(chatMessageRepository);

        verifyZeroInteractions(chatMessageToChatMessageCommand);
    }

    @Test
    void createNewMessage() {
        String username = "username";
        String message = "message";
        Long roomId = 1L;

        User user = User.builder().id(1L).username(username).build();
        Room room = Room.builder().id(roomId).build();

        ChatMessage createdMessage = ChatMessage.builder()
                .message(message)
                .user(user)
                .room(room)
                .build();

        ChatMessage savedMessage = ChatMessage.builder()
                .message(message)
                .user(user)
                .room(room)
                .created(LocalDateTime.now())
                .id(4L)
                .build();

        ChatMessageCommand convertedMessage = ChatMessageCommand.builder().id(5L).build();

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user));
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(chatMessageRepository.save(createdMessage)).thenReturn(savedMessage);
        when(chatMessageToChatMessageCommand.convert(savedMessage)).thenReturn(convertedMessage);

        ChatMessageCommand chatMessageCommand = chatMessageService.createNewMessage(message, username, roomId);

        assertEquals(convertedMessage, chatMessageCommand);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(roomRepository, times(1)).findById(roomId);
        verifyNoMoreInteractions(roomRepository);

        verify(chatMessageRepository, times(1)).save(createdMessage);
        verifyNoMoreInteractions(chatMessageRepository);

        verify(chatMessageToChatMessageCommand, times(1)).convert(savedMessage);
        verifyNoMoreInteractions(chatMessageToChatMessageCommand);
    }

    @Test
    void createNewMessageTrimTest() {
        String username = "username";
        String message = "       message               ";
        Long roomId = 1L;

        User user = User.builder().id(1L).username(username).build();
        Room room = Room.builder().id(roomId).build();

        ChatMessage createdMessage = ChatMessage.builder()
                .message(message.trim())
                .user(user)
                .room(room)
                .build();

        ChatMessage savedMessage = ChatMessage.builder()
                .message(message.trim())
                .user(user)
                .room(room)
                .created(LocalDateTime.now())
                .id(4L)
                .build();

        ChatMessageCommand convertedMessage = ChatMessageCommand.builder().id(5L).build();

        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user));
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(chatMessageRepository.save(createdMessage)).thenReturn(savedMessage);
        when(chatMessageToChatMessageCommand.convert(savedMessage)).thenReturn(convertedMessage);

        ChatMessageCommand chatMessageCommand = chatMessageService.createNewMessage(message, username, roomId);

        assertEquals(convertedMessage, chatMessageCommand);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(roomRepository, times(1)).findById(roomId);
        verifyNoMoreInteractions(roomRepository);

        verify(chatMessageRepository, times(1)).save(createdMessage);
        verifyNoMoreInteractions(chatMessageRepository);

        verify(chatMessageToChatMessageCommand, times(1)).convert(savedMessage);
        verifyNoMoreInteractions(chatMessageToChatMessageCommand);
    }
}