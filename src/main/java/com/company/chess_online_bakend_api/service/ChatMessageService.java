package com.company.chess_online_bakend_api.service;

import com.company.chess_online_bakend_api.data.command.ChatMessageCommand;

import java.util.List;

public interface ChatMessageService {

    long getMessageCountForRoom(Long roomId);

    List<ChatMessageCommand> getMessagePageForRoom(Long roomId, int page);

    ChatMessageCommand createNewMessage(String message, String username, Long roomId);
}
