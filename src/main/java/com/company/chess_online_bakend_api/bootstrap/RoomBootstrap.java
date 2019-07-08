package com.company.chess_online_bakend_api.bootstrap;

import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RoomBootstrap implements CommandLineRunner {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomBootstrap(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadRooms();
    }

    private void loadRooms() {
        List<Room> rooms = new ArrayList<>();

        rooms.add(newRoom("Alpha"));
        rooms.add(newRoom("Beta"));
        rooms.add(newRoom("Gamma"));
        rooms.add(newRoom("Delta"));
        rooms.add(newRoom("Epsilon"));
        rooms.add(newRoom("Zeta"));
        rooms.add(newRoom("Eta"));
        rooms.add(newRoom("Theta"));
        rooms.add(newRoom("Jota"));

        roomRepository.saveAll(rooms);

        log.debug("Rooms loaded = " + roomRepository.count());
    }

    private Room newRoom(String name) {
        return Room.builder().name(name).build();
    }
}
