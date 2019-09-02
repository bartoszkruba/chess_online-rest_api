/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.RoomCommand;
import com.company.chess_online_bakend_api.data.converter.command.room.RoomCommandToRoom;
import com.company.chess_online_bakend_api.data.converter.command.room.RoomToRoomCommand;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.exception.RoomNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RoomServiceJpaImplTest {

    private final Long COUNT = 10L;
    private final Room ROOM1 = Room.builder().name("Alpha").id(1L).build();
    private final Room ROOM2 = Room.builder().name("Beta").id(2L).build();

    private final RoomCommand ROOMCOMMAND1 = RoomCommand.builder().name("Alpha").id(1L).build();
    private final RoomCommand ROOMCOMMAND2 = RoomCommand.builder().name("Beta").id(2L).build();

    private final RoomCommand ROOMCOMMAND1_WITHOUT_GAME = RoomCommand.builder()
            .name("Alpha")
            .gameStatus(GameStatus.WAITNG_TO_START)
            .id(1L).build();

    private final RoomCommand ROOMCOMMAND2_WITHOUT_GAME = RoomCommand.builder()
            .name("Beta")
            .gameStatus(GameStatus.WAITNG_TO_START)
            .id(1L).build();

    @Mock
    RoomRepository roomRepository;

    @Mock
    RoomToRoomCommand roomToRoomCommand;

    @Mock
    RoomCommandToRoom roomCommandToRoom;

    @InjectMocks
    RoomServiceJpaImpl roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(roomToRoomCommand.convert(ROOM1)).thenReturn(ROOMCOMMAND1);
        when(roomToRoomCommand.convert(ROOM2)).thenReturn(ROOMCOMMAND2);

        when(roomToRoomCommand.convertWithoutGame(ROOM1)).thenReturn(ROOMCOMMAND1_WITHOUT_GAME);
        when(roomToRoomCommand.convertWithoutGame(ROOM2)).thenReturn(ROOMCOMMAND2_WITHOUT_GAME);

        when(roomCommandToRoom.convert(ROOMCOMMAND1)).thenReturn(ROOM1);
        when(roomCommandToRoom.convert(ROOMCOMMAND2)).thenReturn(ROOM2);

        when(roomRepository.findById(ROOM2.getId())).thenReturn(Optional.of(ROOM2));
        when(roomRepository.findById(ROOM1.getId())).thenReturn(Optional.of(ROOM1));

        when(roomRepository.save(ROOM1)).thenReturn(ROOM1);

        when(roomRepository.findAll()).thenReturn(Arrays.asList(ROOM1, ROOM2));
    }

    @Test
    void getRoomCount() {
        when(roomRepository.count()).thenReturn(COUNT);

        Long count = roomService.getRoomCount();

        assertEquals(COUNT, count);
        verify(roomRepository, times(1)).count();
        verifyNoMoreInteractions(roomRepository);
        verifyZeroInteractions(roomCommandToRoom);
        verifyZeroInteractions(roomToRoomCommand);
    }

    @Test
    void createNewRoom() {
        when(roomRepository.findByNameLike(ROOMCOMMAND1.getName())).thenReturn(Optional.empty());
        when(roomRepository.save(any())).thenReturn(ROOM1);

        RoomCommand roomCommand = roomService.createNewRoom(ROOMCOMMAND1);

        assertEquals(ROOMCOMMAND1, roomCommand);

        verify(roomRepository, times(1)).save(any());
        verifyNoMoreInteractions(roomRepository);

        verify(roomToRoomCommand, times(1)).convert(ROOM1);
        verifyNoMoreInteractions(roomToRoomCommand);
    }

    @Test
    void getRoomPage() {

        List<Room> rooms = Arrays.asList(ROOM1, ROOM2);
        Page roomsPage = new PageImpl(rooms);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("name").ascending());
        when(roomRepository.findAll(pageRequest)).thenReturn(roomsPage);

        Set<RoomCommand> roomList = roomService.getRoomPage(0).getRooms();

        assertEquals(2, roomList.size());
        assertTrue(roomList.contains(ROOMCOMMAND1_WITHOUT_GAME));
        assertTrue(roomList.contains(ROOMCOMMAND2_WITHOUT_GAME));

        verify(roomRepository, times(1)).findAll(pageRequest);
        verify(roomRepository, times(1)).count();
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void findById() {
        RoomCommand roomCommand = roomService.findById(ROOM1.getId());
        assertEquals(roomCommand, ROOMCOMMAND1);

        verify(roomRepository, times(1)).findById(ROOM1.getId());
        verifyNoMoreInteractions(roomRepository);

        verify(roomToRoomCommand, times(1)).convert(ROOM1);
        verifyNoMoreInteractions(roomToRoomCommand);
        verifyZeroInteractions(roomCommandToRoom);
    }

    @Test
    void findByIdNotFound() {

        Assertions.assertThrows(RoomNotFoundException.class, () -> roomService.findById(24390L));

        verify(roomRepository, times(1)).findById(24390L);
        verifyNoMoreInteractions(roomRepository);
        verifyZeroInteractions(roomCommandToRoom);
        verifyZeroInteractions(roomToRoomCommand);
    }

    @Test
    void save() {
        RoomCommand roomCommand = roomService.save(ROOMCOMMAND1);

        assertEquals(roomCommand, ROOMCOMMAND1);

        verify(roomRepository, times(1)).save(ROOM1);
        verifyNoMoreInteractions(roomRepository);

        verify(roomCommandToRoom, times(1)).convert(ROOMCOMMAND1);
        verifyNoMoreInteractions(roomCommandToRoom);

        verify(roomToRoomCommand, times(1)).convert(ROOM1);
        verifyNoMoreInteractions(roomToRoomCommand);
    }

    @Test
    void findAll() {
        Set<RoomCommand> roomCommands = roomService.findAll();

        assertEquals(2, roomCommands.size());
        assertTrue(roomCommands.contains(ROOMCOMMAND1));
        assertTrue(roomCommands.contains(ROOMCOMMAND2));

        verify(roomRepository, times(1)).findAll();
        verifyNoMoreInteractions(roomRepository);

        verify(roomToRoomCommand, times(1)).convert(ROOM1);
        verify(roomToRoomCommand, times(1)).convert(ROOM2);
        verifyNoMoreInteractions(roomToRoomCommand);

        verifyNoMoreInteractions(roomCommandToRoom);
    }

    @Test
    void delete() {
        roomService.delete(ROOMCOMMAND1);

        verify(roomRepository, times(1)).findById(ROOMCOMMAND1.getId());
        verify(roomRepository, times(1)).delete(ROOM1);
        verifyNoMoreInteractions(roomRepository);

        verifyZeroInteractions(roomToRoomCommand);
        verifyZeroInteractions(roomCommandToRoom);
    }

    @Test
    void deleteRoomNotFound() {
        when(roomRepository.findById(ROOMCOMMAND1.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(RoomNotFoundException.class, () -> {
            roomService.delete(ROOMCOMMAND1);
        });
    }

    @Test
    void deleteById() {
        roomService.deleteById(ROOMCOMMAND1.getId());

        verify(roomRepository, times(1)).findById(ROOMCOMMAND1.getId());
        verify(roomRepository, times(1)).delete(ROOM1);
        verifyNoMoreInteractions(roomRepository);

        verifyZeroInteractions(roomToRoomCommand);
        verifyZeroInteractions(roomCommandToRoom);
    }

    @Test
    void deleteByIdRoomNotFound() {
        when(roomRepository.findById(ROOMCOMMAND1.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(RoomNotFoundException.class, () -> {
            roomService.deleteById(ROOMCOMMAND1.getId());
        });
    }
}