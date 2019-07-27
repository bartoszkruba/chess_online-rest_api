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
import com.company.chess_online_bakend_api.data.converter.command.chatMessage.ChatMessageToChatMessageCommand;
import com.company.chess_online_bakend_api.data.model.ChatMessage;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.ChatMessageRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.exception.RoomNotFoundException;
import com.company.chess_online_bakend_api.exception.UserNotFoundException;
import com.company.chess_online_bakend_api.service.ChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatMessageServiceJpaImpl implements ChatMessageService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageToChatMessageCommand chatMessageToChatMessageCommand;

    @Autowired
    public ChatMessageServiceJpaImpl(UserRepository userRepository, RoomRepository roomRepository,
                                     ChatMessageRepository chatMessageRepository,
                                     ChatMessageToChatMessageCommand chatMessageToChatMessageCommand) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageToChatMessageCommand = chatMessageToChatMessageCommand;
    }

    @Override
    public long getMessageCountForRoom(Long roomId) {
        log.debug("Getting message count for room with id " + roomId);

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room with id " + roomId + " does not exist"));

        return chatMessageRepository.countByRoom(room);
    }

    @Override
    public List<ChatMessageCommand> getMessagePageForRoom(Long roomId, int page) {
        log.debug("Getting messages from room: " + roomId + ", page: " + page);

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room with id " + roomId + " does not exist"));

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("created").descending());

        return chatMessageRepository.findByRoom(room, pageRequest).stream()
                .map(chatMessageToChatMessageCommand::convert)
                .collect(Collectors.toList());
    }

    @Override
    public ChatMessageCommand createNewMessage(String message, String username, Long roomId) {
        log.debug("Creating new message for room: " + roomId + ", username: " + username);

        User user = userRepository.findByUsernameLike(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " does not exist."));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room with id " + roomId + " does not exist."));

        ChatMessage chatMessage = ChatMessage.builder()
                .message(message.trim())
                .user(user)
                .room(room)
                .build();

        ChatMessageCommand chatMessageCommand = chatMessageToChatMessageCommand
                .convert(chatMessageRepository.save(chatMessage));

        // TODO: 2019-07-26 Stream message through sockets

        return chatMessageCommand;
    }
}
