/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.bootstrap.dev.RoomBootstrap;
import com.company.chess_online_bakend_api.data.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("dev")
class RoomRepositoryIntegrationTest {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    RoomBootstrap roomBootstrap;


    @BeforeEach
    void setUp() throws Exception {
        roomBootstrap.run();
    }

    @AfterEach
    void tearDown() {
        roomRepository.deleteAll();
    }

    @Test
    void findByName() {
        Optional<Room> roomOptional = roomRepository.findByNameLike("Room 1");

        assertEquals("Room 1", roomOptional.get().getName());
    }

    @Test
    @Transactional
    void findByGame() {
        Room room = roomRepository.findByNameLike("Room 1").get();

        Room foundRoom = roomRepository.findRoomByGame(room.getGame()).get();

        assertEquals(room, foundRoom);
    }

    @Test
    void findByNameNoMatch() {
        Optional<Room> roomOptional = roomRepository.findByNameLike("Does not exists");

        assertTrue(roomOptional.isEmpty());
    }

    @Test
    void getRoomPage() {
        Pageable pageRequest = PageRequest.of(0, 5, Sort.by("name").ascending());
        Page<Room> rooms = roomRepository.findAll(pageRequest);

        assertEquals(5, rooms.get().count());
        Optional<Room> roomOptional = rooms.stream()
                .filter(r -> r.getName().equals("Room 1"))
                .findFirst();
        assertTrue(roomOptional.isPresent());
    }
}