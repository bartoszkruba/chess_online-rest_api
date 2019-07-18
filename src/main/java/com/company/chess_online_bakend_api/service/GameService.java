package com.company.chess_online_bakend_api.service;

import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.model.enums.PieceColor;

public interface GameService extends CrudService<GameCommand, Long> {

    GameCommand getByRoomId(Long id);

    GameCommand joinGame(PieceColor color, String username, Long gameId);

    GameCommand leaveGame(String username, Long gameId);
}
