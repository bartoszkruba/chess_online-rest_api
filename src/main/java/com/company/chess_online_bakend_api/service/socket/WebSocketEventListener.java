/*
 * 8/4/19, 3:54 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service.socket;

import com.company.chess_online_bakend_api.data.model.WebSocketId;
import com.company.chess_online_bakend_api.data.repository.WebSocketIdRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
public class WebSocketEventListener {

    private final WebSocketIdRepository webSocketIdRepository;

    @Autowired
    public WebSocketEventListener(WebSocketIdRepository webSocketIdRepository) {
        this.webSocketIdRepository = webSocketIdRepository;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        var user = event.getUser();
        String id = (user != null) ? user.getName() : "";

        log.info("Received a new web socket connection, id: " + id);

        var webSocketId = WebSocketId.builder().connectionId(id).build();

        log.debug("Saving Web Socket id in database: " + id);
        webSocketIdRepository.save(webSocketId);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // TODO: 2019-08-04 do something about it?

        var user = event.getUser();
        String id = (user != null) ? user.getName() : "";

        log.info("Web socket disconnected, id: " + id);
    }
}
