/*
 * 7/27/19 3:52 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service;

import com.company.chess_online_bakend_api.data.model.ChatMessage;
import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.User;

public interface SocketService {

    void broadcastChatMessage(ChatMessage chatMessage);

    void broadcastJoinGame(Game game, User user, Long roomId);

    void broadcastLeaveGame(Game game, User user, Long roomId);
}
