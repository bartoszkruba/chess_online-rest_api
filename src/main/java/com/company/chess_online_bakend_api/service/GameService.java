/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service;

import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.model.enums.PieceColor;

public interface GameService extends CrudService<GameCommand, Long> {

    GameCommand getByRoomId(Long id);

    GameCommand joinGame(PieceColor color, String username, Long gameId);

    GameCommand leaveGame(String username, Long gameId);
}
