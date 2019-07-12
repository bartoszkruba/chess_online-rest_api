package com.company.chess_online_bakend_api.data.converter.room;

import com.company.chess_online_bakend_api.data.command.RoomCommand;
import com.company.chess_online_bakend_api.data.converter.game.GameToGameCommand;
import com.company.chess_online_bakend_api.data.model.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoomToRoomCommand implements Converter<Room, RoomCommand> {

    private final GameToGameCommand gameToGameCommand;

    @Autowired
    public RoomToRoomCommand(GameToGameCommand gameToGameCommand) {
        this.gameToGameCommand = gameToGameCommand;
    }

    @Override
    @Nullable
    public RoomCommand convert(Room room) {

        log.debug("Converting Room to RoomCommand");

        if (room == null) {
            return null;
        }

        RoomCommand roomCommand = RoomCommand.builder()
                .id(room.getId())
                .name(room.getName())
                .build();

        if (room.getGame() != null) {
            roomCommand.setGame(gameToGameCommand.convert(room.getGame()));
        }

        return roomCommand;
    }
}
