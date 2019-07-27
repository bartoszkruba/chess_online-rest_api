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
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.exception.RoomNotFoundException;
import com.company.chess_online_bakend_api.service.RoomService;
import com.company.chess_online_bakend_api.util.GameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoomServiceJpaImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomToRoomCommand roomToRoomCommand;
    private final RoomCommandToRoom roomCommandToRoom;

    @Autowired
    public RoomServiceJpaImpl(RoomRepository roomRepository, RoomToRoomCommand roomToRoomCommand,
                              RoomCommandToRoom roomCommandToRoom) {
        this.roomRepository = roomRepository;
        this.roomToRoomCommand = roomToRoomCommand;
        this.roomCommandToRoom = roomCommandToRoom;
    }

    @Override
    public long getRoomCount() {
        log.debug("Getting room count");
        return roomRepository.count();
    }

    @Override
    public Set<RoomCommand> getRoomPage(int page) {
        log.debug("Getting room page " + page);

        Pageable pageRequest = PageRequest.of(page, 10, Sort.by("name").ascending());
        Page<Room> roomPage = roomRepository.findAll(pageRequest);
        return roomPage.get()
                // Don't want to send room with game and board
                // and all pieces etc if we just gonna send list with rooms.
                .map(roomToRoomCommand::convertWithoutGame)
                .peek(System.out::println)
                .collect(Collectors.toSet());
    }

    @Override
    public RoomCommand createNewRoom(RoomCommand roomCommand) {
        log.debug("Creating new room");

        Room room = roomCommandToRoom.convert(roomCommand);
        room.setId(null);
        room.addGame(GameUtil.initNewGame());
        return roomToRoomCommand.convert(roomRepository.save(room));
    }

    @Override
    public RoomCommand findById(Long id) {
        log.debug("Finding room by id " + id);

        Optional<Room> roomOptional = roomRepository.findById(id);

        if (roomOptional.isEmpty()) {
            throw new RoomNotFoundException("Room with id " + id + " does not exists");
        }

        return roomOptional.map(roomToRoomCommand::convert).orElse(null);
    }

    // TODO: 2019-07-15 implement REST path for admins only

    @Override
    public RoomCommand save(RoomCommand roomCommand) {
        log.debug("Saving room");

        Room room = roomCommandToRoom.convert(roomCommand);

        return roomToRoomCommand.convert(roomRepository.save(room));
    }

    // TODO: 2019-07-15 implement REST path for admins only

    @Override
    public Set findAll() {
        log.debug("Finding all rooms");
        Set<RoomCommand> roomCommands = new HashSet<>();
        roomRepository.findAll().forEach(room -> roomCommands.add(roomToRoomCommand.convert(room)));

        return roomCommands;
    }

    // TODO: 2019-07-15 implement REST path for admins only

    @Override
    public void delete(RoomCommand roomCommand) {
        log.debug("Deleting room");
        deleteById(roomCommand.getId());
    }

    // TODO: 2019-07-15 implement Rest path for admins only

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting room with id " + id);
        Optional<Room> roomOptional = roomRepository.findById(id);

        if (roomOptional.isEmpty()) {
            throw new RoomNotFoundException("Room does not exist");
        }

        roomRepository.delete(roomOptional.get());
    }

}
