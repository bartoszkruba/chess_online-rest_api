package com.company.chess_online_bakend_api.bootstrap.dev;

import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Profile("dev")
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

        if (roomRepository.findByNameLike("Alpha").isEmpty()) {
            rooms.add(newRoom("Alpha"));
        }
        if (roomRepository.findByNameLike("Beta").isEmpty()) {
            rooms.add(newRoom("Beta"));
        }
        if (roomRepository.findByNameLike("Gamma").isEmpty()) {
            rooms.add(newRoom("Gamma"));
        }
        if (roomRepository.findByNameLike("Delta").isEmpty()) {
            rooms.add(newRoom("Delta"));
        }
        if (roomRepository.findByNameLike("Epsilon").isEmpty()) {
            rooms.add(newRoom("Epsilon"));
        }
        if (roomRepository.findByNameLike("Zeta").isEmpty()) {
            rooms.add(newRoom("Zeta"));
        }
        if (roomRepository.findByNameLike("Eta").isEmpty()) {
            rooms.add(newRoom("Eta"));
        }
        if (roomRepository.findByNameLike("Theta").isEmpty()) {
            rooms.add(newRoom("Theta"));
        }
        if (roomRepository.findByNameLike("Jota").isEmpty()) {
            rooms.add(newRoom("Jota"));
        }

        roomRepository.saveAll(rooms);

        log.debug("Rooms loaded = " + roomRepository.count());
    }

    private Room newRoom(String name) {
        return Room.builder().name(name).build();
    }
}
