package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.ChatMessageCommand;
import com.company.chess_online_bakend_api.data.converter.chatMessage.ChatMessageToChatMessageCommand;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.repository.ChatMessageRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.exception.RoomNotFoundException;
import com.company.chess_online_bakend_api.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageServiceJpaImpl implements ChatMessageService {

    private final RoomRepository roomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageToChatMessageCommand chatMessageToChatMessageCommand;

    @Autowired
    public ChatMessageServiceJpaImpl(RoomRepository roomRepository, ChatMessageRepository chatMessageRepository,
                                     ChatMessageToChatMessageCommand chatMessageToChatMessageCommand) {
        this.roomRepository = roomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageToChatMessageCommand = chatMessageToChatMessageCommand;
    }

    @Override
    public long getMessageCountForRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room with id " + roomId + " does not exist"));

        return chatMessageRepository.countByRoom(room);
    }

    @Override
    public List<ChatMessageCommand> getMessagePageForRoom(Long roomId, int page) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room with id " + roomId + " does not exist"));

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("created").descending());

        return chatMessageRepository.findByRoom(room, pageRequest).stream()
                .map(chatMessageToChatMessageCommand::convert)
                .collect(Collectors.toList());
    }

    @Override
    public ChatMessageCommand createNewMessage(String message, String username, Long roomId) {
        return null;
    }
}
