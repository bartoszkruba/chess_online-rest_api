/*
 * 8/24/19, 4:23 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.controller;

import com.company.chess_online_bakend_api.service.socket.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
public class SocketController {

    private final SocketService socketService;

    @Autowired
    public SocketController(SocketService socketService) {
        this.socketService = socketService;
    }

    @MessageMapping("/game.{gameId}.ping")
    public void receivePlayerPing(@DestinationVariable Long gameId, Principal principal) {
        log.debug("New notification: /game." + gameId + ".ping, from user: " + principal.getName());

        socketService.updatePlayerPingInGame(gameId, principal.getName());
    }
}
