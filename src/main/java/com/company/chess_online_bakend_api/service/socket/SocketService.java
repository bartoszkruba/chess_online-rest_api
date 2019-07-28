/*
 * 7/28/19 4:57 PM. Updated by Bartosz Kruba.
 */

/*
 * 7/27/19 3:52 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service.socket;

import com.company.chess_online_bakend_api.data.model.ChatMessage;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.model.enums.PieceColor;

public interface SocketService {

    void broadcastChatMessage(ChatMessage chatMessage);

    void broadcastJoinGame(User user, Long gameId, PieceColor color, String fenNotation, Long roomId);

    void broadcastLeaveGame(User user, Long gameId, PieceColor color, String fenNotation, Long roomId);
}
