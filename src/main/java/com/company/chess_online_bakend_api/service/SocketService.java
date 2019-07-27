/*
 * 7/27/19 3:52 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service;

import com.company.chess_online_bakend_api.data.model.ChatMessage;

public interface SocketService {

    void broadcastChatMessage(ChatMessage chatMessage);
}
