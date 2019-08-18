/*
 * 8/18/19, 6:58 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.model;

import com.company.chess_online_bakend_api.bootstrap.dev.RoomBootstrap;
import com.company.chess_online_bakend_api.data.repository.ChatMessageRepository;
import com.company.chess_online_bakend_api.data.repository.GameRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class VersionAnnotationIT {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private RoomBootstrap roomBootstrap;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() throws Exception {
        roomBootstrap.run();
    }

    @AfterEach
    void tearDown() {
        roomRepository.deleteAll();
    }

    @Test
    void gameVersionAnnotationTest() {
        var room = roomRepository.findByNameLike("Alpha").orElseThrow(() -> new RuntimeException("No room found"));
        var game = gameRepository.findGameByRoom(room).orElseThrow(() -> new RuntimeException("No game found"));

        game.setFenNotation("ddd");
        gameRepository.save(game);
        game.setFenNotation("eee");

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> gameRepository.save(game));
    }

    @Test
    void roomVersionAnnotationTest() {
        var room = roomRepository.findByNameLike("Alpha").orElseThrow(() -> new RuntimeException("No room found"));

        room.setName("dddd");
        roomRepository.save(room);
        room.setName("eeeee");
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> roomRepository.save(room));
    }
}
