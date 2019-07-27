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
import com.company.chess_online_bakend_api.data.converter.command.game.GameCommandToGame;
import com.company.chess_online_bakend_api.data.model.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoomCommandToRoom implements Converter<RoomCommand, Room> {

    private final GameCommandToGame gameCommandToGame;

    @Autowired
    public RoomCommandToRoom(GameCommandToGame gameCommandToGame) {
        this.gameCommandToGame = gameCommandToGame;
    }

    @Override
    @Nullable
    public Room convert(RoomCommand roomCommand) {

        log.debug("Converting RoomCommand to Room");

        if (roomCommand == null) {
            return null;
        }

        var room = Room.builder()
                .id(roomCommand.getId())
                .name(roomCommand.getName()).build();

        if (roomCommand.getGame() != null) {
            room.setGame(gameCommandToGame.convert(roomCommand.getGame()));
        } else {
            room.setGame(null);
        }

        return room;
    }
}
