package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.bootstrap.RoomBootstrap;
import com.company.chess_online_bakend_api.data.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRepository;

    @BeforeEach
    void setUp() throws Exception {

        roomRepository.deleteAll();

        RoomBootstrap roomBootstrap = new RoomBootstrap(roomRepository);
        roomBootstrap.run();
    }

    @Test
    void findByName() {
        Optional<Room> roomOptional = roomRepository.findByNameLike("Alpha");

        assertEquals("Alpha", roomOptional.get().getName());
    }

    @Test
    void findByNameNoMatch() {
        Optional<Room> roomOptional = roomRepository.findByNameLike("Does not exists");

        assertTrue(roomOptional.isEmpty());
    }
}