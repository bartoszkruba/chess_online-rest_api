package com.company.chess_online_bakend_api.data.converter;

import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.command.RoomCommand;
import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.converter.game.GameToGameCommand;
import com.company.chess_online_bakend_api.data.converter.room.RoomToRoomCommand;
import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.GameStatus;
import com.company.chess_online_bakend_api.data.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoomToRoomCommandTest {

    private final Long ROOM_ID = 1L;
    private final String ROOM_NAME = "Room Name";

    private final GameCommand GAME_COMMAND = GameCommand.builder()
            .id(2L)
            .status(GameStatus.STARTED)
            .turn(1)
            .blackPlayer(UserCommand.builder().id(3L).build())
            .whitePlayer(UserCommand.builder().id(4L).build())
            .roomId(ROOM_ID).build();

    @Mock
    GameToGameCommand gameToGameCommand;

    @InjectMocks
    RoomToRoomCommand roomToRoomCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testConvertNullObject() {
        RoomCommand roomCommand = roomToRoomCommand.convert(null);

        assertNull(roomCommand);

        verify(gameToGameCommand, times(0)).convert(any());
    }

    @Test
    void testConvertEmptyObject() {
        Room room = Room.builder().build();
        room.setGame(null);

        RoomCommand roomCommand = roomToRoomCommand.convert(room);

        assertNull(roomCommand.getId());
        assertNull(roomCommand.getName());
        assertNull(roomCommand.getGame());

        verify(gameToGameCommand, times(0)).convert(any());
    }

    @Test
    void testConvert() {
        Game game = Game.builder().id(4L).build();

        when(gameToGameCommand.convert(game)).thenReturn(GAME_COMMAND);

        Room room = Room.builder()
                .id(ROOM_ID)
                .name(ROOM_NAME).build();

        room.setGame(game);

        RoomCommand roomCommand = roomToRoomCommand.convert(room);

        assertEquals(ROOM_ID, roomCommand.getId());
        assertEquals(ROOM_NAME, roomCommand.getName());
        assertEquals(GAME_COMMAND, roomCommand.getGame());

        verify(gameToGameCommand, times(1)).convert(game);
    }
}