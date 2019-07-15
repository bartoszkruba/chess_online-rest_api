package com.company.chess_online_bakend_api.service;

import com.company.chess_online_bakend_api.data.command.GameCommand;

public interface GameService extends CrudService<GameCommand, Long> {

    GameCommand getByRoomId(Long id);
}
