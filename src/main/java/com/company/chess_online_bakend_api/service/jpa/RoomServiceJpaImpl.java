package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.RoomCommand;
import com.company.chess_online_bakend_api.data.converter.room.RoomCommandToRoom;
import com.company.chess_online_bakend_api.data.converter.room.RoomToRoomCommand;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.exception.RoomNotFoundException;
import com.company.chess_online_bakend_api.service.RoomService;
import com.company.chess_online_bakend_api.util.GameUtil;
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
        return roomRepository.count();
    }

    @Override
    public Set<RoomCommand> getRoomPage(int page) {
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
        Room room = roomCommandToRoom.convert(roomCommand);
        room.setId(null);
        room.addGame(GameUtil.initNewGame());
        return roomToRoomCommand.convert(roomRepository.save(room));
    }

    @Override
    public RoomCommand findById(Long id) {
        Optional<Room> roomOptional = roomRepository.findById(id);

        if (roomOptional.isEmpty()) {
            throw new RoomNotFoundException("Room with id " + id + " does not exists");
        }

        return roomOptional.map(roomToRoomCommand::convert).orElse(null);
    }

    @Override
    public RoomCommand save(RoomCommand roomCommand) {
        Room room = roomCommandToRoom.convert(roomCommand);

        return roomToRoomCommand.convert(roomRepository.save(room));
    }

    @Override
    public Set findAll() {
        Set<RoomCommand> roomCommands = new HashSet<>();
        roomRepository.findAll().forEach(room -> roomCommands.add(roomToRoomCommand.convert(room)));

        return roomCommands;
    }

    @Override
    public void delete(RoomCommand roomCommand) {
        deleteById(roomCommand.getId());
    }

    @Override
    public void deleteById(Long id) {
        Optional<Room> roomOptional = roomRepository.findById(id);

        if (roomOptional.isEmpty()) {
            throw new RoomNotFoundException("Room does not exist");
        }

        roomRepository.delete(roomOptional.get());
    }

}
