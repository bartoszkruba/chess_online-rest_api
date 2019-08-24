/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service;

import com.company.chess_online_bakend_api.data.command.ChatMessageCommand;
import com.company.chess_online_bakend_api.data.command.ChatMessagePageCommand;

public interface ChatMessageService {

    long getMessageCountForRoom(Long roomId);

    ChatMessagePageCommand getMessagePageForRoom(Long roomId, int page);

    ChatMessageCommand createNewMessage(String message, String username, Long roomId);
}
