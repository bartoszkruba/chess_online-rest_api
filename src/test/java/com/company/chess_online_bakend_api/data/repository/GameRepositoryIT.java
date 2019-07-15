package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.bootstrap.dev.RoomBootstrap;
import com.company.chess_online_bakend_api.data.model.Game;
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
public class GameRepositoryIT {

    private final Long ROOM_ID = 1L;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    GameRepository gameRepository;

    @BeforeEach
    void setUp() throws Exception {
        RoomBootstrap roomBootstrap = new RoomBootstrap(roomRepository);
        roomBootstrap.run();
    }

    @Test
    void findByRoom() {
        Optional<Game> optionalGame = gameRepository.findGameByRoom(Room.builder().id(ROOM_ID).build());

        assertTrue(optionalGame.isPresent());
        assertEquals(ROOM_ID, optionalGame.get().getRoom().getId());
    }
}
