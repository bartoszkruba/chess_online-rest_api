/*
 * 8/31/19, 5:50 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.data.model.WebSocketId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("dev")
class WebSocketIdRepositoryTest {

    @Autowired
    private WebSocketIdRepository webSocketIdRepository;

    private String connectionId;

    @BeforeEach
    void setUp() {
        connectionId = UUID.randomUUID().toString();
        var webSocketId = WebSocketId.builder().connectionId(connectionId).build();
        webSocketIdRepository.save(webSocketId);
    }

    @Test
    void findByConnectionId() {
        var webSocketId = webSocketIdRepository.findById(connectionId)
                .orElseThrow(() -> new RuntimeException("Not Found"));

        assertEquals(webSocketId.getConnectionId(), connectionId);
    }


    @AfterEach
    void tearDown() {
        webSocketIdRepository.deleteAll();
    }
}