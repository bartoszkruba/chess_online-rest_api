/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.room;

import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.command.RoomCommand;
import com.company.chess_online_bakend_api.data.converter.game.GameCommandToGame;
import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class RoomCommandToRoomTest {

    private final Long ROOM_ID = 1L;
    private final String ROOM_NAME = "Room Name";

    private final Game GAME = Game.builder()
            .id(2L)
            .status(GameStatus.STARTED)
            .turn(1)
            .blackPlayer(User.builder().id(3L).build())
            .whitePlayer(User.builder().id(4L).build()).build();

    @Mock
    GameCommandToGame gameCommandToGame;

    @InjectMocks
    RoomCommandToRoom roomCommandToRoom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void convertObjectNull() {
        Room room = roomCommandToRoom.convert(null);

        assertNull(room);

        verifyNoMoreInteractions(gameCommandToGame);
    }

    @Test
    void convertEmptyObject() {
        Room room = roomCommandToRoom.convert(RoomCommand.builder().build());

        assertNull(room.getId());
        assertNull(room.getName());
        assertNull(room.getGame());

        verifyZeroInteractions(gameCommandToGame);
    }

    @Test
    void convert() {
        GameCommand gameCommand = GameCommand.builder().id(5L).build();

        when(gameCommandToGame.convert(gameCommand)).thenReturn(GAME);

        RoomCommand roomCommand = RoomCommand.builder()
                .id(ROOM_ID)
                .name(ROOM_NAME)
                .game(gameCommand).build();

        Room room = roomCommandToRoom.convert(roomCommand);

        assertEquals(ROOM_ID, room.getId());
        assertEquals(ROOM_NAME, room.getName());
        assertEquals(GAME, room.getGame());

        verify(gameCommandToGame, times(1)).convert(gameCommand);
    }
}