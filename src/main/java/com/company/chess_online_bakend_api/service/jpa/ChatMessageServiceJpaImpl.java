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
import com.company.chess_online_bakend_api.data.command.ChatMessagePageCommand;
import com.company.chess_online_bakend_api.data.converter.command.chatMessage.ChatMessageToChatMessageCommand;
import com.company.chess_online_bakend_api.data.model.ChatMessage;
import com.company.chess_online_bakend_api.data.repository.ChatMessageRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.exception.RoomNotFoundException;
import com.company.chess_online_bakend_api.exception.UserNotFoundException;
import com.company.chess_online_bakend_api.service.ChatMessageService;
import com.company.chess_online_bakend_api.service.socket.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatMessageServiceJpaImpl implements ChatMessageService {

    private final SocketService socketService;

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ChatMessageRepository chatMessageRepository;

    private final ChatMessageToChatMessageCommand chatMessageToChatMessageCommand;

    @Autowired
    public ChatMessageServiceJpaImpl(SocketService socketService, UserRepository userRepository,
                                     RoomRepository roomRepository, ChatMessageRepository chatMessageRepository,
                                     ChatMessageToChatMessageCommand chatMessageToChatMessageCommand) {
        this.socketService = socketService;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageToChatMessageCommand = chatMessageToChatMessageCommand;
    }

    @Override
    public long getMessageCountForRoom(Long roomId) {
        log.debug("Getting message count for room with id " + roomId);

        var room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room with id " + roomId + " does not exist"));

        return chatMessageRepository.countByRoom(room);
    }

    @Override
    public ChatMessagePageCommand getMessagePageForRoom(Long roomId, int page) {
        log.debug("Getting messages from room: " + roomId + ", page: " + page);

        var room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room with id " + roomId + " does not exist"));

        var pageRequest = PageRequest.of(page, 10, Sort.by("created").descending());

        var messages = chatMessageRepository.findByRoom(room, pageRequest)
                .stream()
                .map(chatMessageToChatMessageCommand::convert)
                .collect(Collectors.toList());

        return ChatMessagePageCommand.builder()
                .messages(messages)
                .page(page)
                .totalMessages((long) room.getChatMessages().size())
                .build();

    }

    @Override
    public ChatMessageCommand createNewMessage(String message, String username, Long roomId) {
        log.debug("Creating new message for room: " + roomId + ", username: " + username);

        var user = userRepository.findByUsernameLike(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " does not exist."));
        var room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room with id " + roomId + " does not exist."));

        var chatMessage = ChatMessage.builder()
                .message(message.trim())
                .user(user)
                .room(room)
                .build();

        var savedChatMessage = chatMessageRepository.save(chatMessage);

        socketService.broadcastChatMessage(savedChatMessage);

        return chatMessageToChatMessageCommand.convert(savedChatMessage);
    }
}
