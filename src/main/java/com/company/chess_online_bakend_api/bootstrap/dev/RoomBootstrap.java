/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:10 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.bootstrap.dev;

import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.util.GameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
@Profile("dev")
public class RoomBootstrap implements CommandLineRunner {

    @Value("${rooms:20}")
    private int roomsToCreate;

    private final RoomRepository roomRepository;

    @Autowired
    public RoomBootstrap(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.debug("Generating rooms...");
        loadRooms();
        log.debug("All rooms created");
    }

    private void loadRooms() {
        List<Room> rooms = new ArrayList<>();

        for (int i = 1; i <= roomsToCreate; i++) {
            final String roomName = "Room " + i;
            if (roomRepository.findByNameLike(roomName).isEmpty()) {
                rooms.add(newRoom(roomName));
            }
        }

//        if (roomRepository.findByNameLike("Alpha").isEmpty()) {
//            rooms.add(newRoom("Alpha"));
//        }
//        if (roomRepository.findByNameLike("Beta").isEmpty()) {
//            rooms.add(newRoom("Beta"));
//        }
//        if (roomRepository.findByNameLike("Gamma").isEmpty()) {
//            rooms.add(newRoom("Gamma"));
//        }
//        if (roomRepository.findByNameLike("Delta").isEmpty()) {
//            rooms.add(newRoom("Delta"));
//        }
//        if (roomRepository.findByNameLike("Epsilon").isEmpty()) {
//            rooms.add(newRoom("Epsilon"));
//        }
//        if (roomRepository.findByNameLike("Zeta").isEmpty()) {
//            rooms.add(newRoom("Zeta"));
//        }
//        if (roomRepository.findByNameLike("Eta").isEmpty()) {
//            rooms.add(newRoom("Eta"));
//        }
//        if (roomRepository.findByNameLike("Theta").isEmpty()) {
//            rooms.add(newRoom("Theta"));
//        }
//        if (roomRepository.findByNameLike("Jota").isEmpty()) {
//            rooms.add(newRoom("Jota"));
//        }

        roomRepository.saveAll(rooms);

        log.info("Rooms loaded = " + roomRepository.count());
    }

    private Room newRoom(String name) {
        log.info("Creating new room: " + name);
        Room room = Room.builder().name(name).build();
        room.addGame(GameUtil.initNewGame());
        return room;
    }
}
