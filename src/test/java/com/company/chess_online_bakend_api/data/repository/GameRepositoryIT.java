/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.bootstrap.dev.RoomBootstrap;
import com.company.chess_online_bakend_api.data.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("dev")
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
        var room = roomRepository.findById(ROOM_ID).orElseThrow(() -> new RuntimeException("Room not found"));
        Optional<Game> optionalGame = gameRepository.findGameByRoom(room);

        assertTrue(optionalGame.isPresent());
        assertEquals(ROOM_ID, optionalGame.get().getRoom().getId());
    }
}
