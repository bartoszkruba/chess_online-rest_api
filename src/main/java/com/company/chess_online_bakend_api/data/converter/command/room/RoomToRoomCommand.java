/*
 * 7/27/19 3:26 PM. Updated by Bartosz Kruba.
 */

/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.command.room;

import com.company.chess_online_bakend_api.data.command.RoomCommand;
import com.company.chess_online_bakend_api.data.converter.command.game.GameToGameCommand;
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

        var roomCommand = RoomCommand.builder()
                .id(room.getId())
                .name(room.getName())
                .build();

        if (room.getGame() != null) {
            roomCommand.setGame(gameToGameCommand.convert(room.getGame()));
        }

        return roomCommand;
    }

    public RoomCommand convertWithoutGame(Room room) {

        log.debug("Converting Room to RoomCommand without Game");

        if (room == null) {
            return null;
        }

        return RoomCommand.builder()
                .id(room.getId())
                .name(room.getName())
                .gameStatus(room.getGame().getStatus())
                .build();
    }
}
