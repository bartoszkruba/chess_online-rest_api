/*
 * 8/4/19, 3:54 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
public class WebSocketEventListener {

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        var user = event.getUser();
        String id = (user != null) ? user.getName() : "";

        log.info("Received a new web socket connection, principal: " + id);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // TODO: 2019-08-04 do something about it?

        var user = event.getUser();
        String id = (user != null) ? user.getName() : "";

        log.info("Web socket disconnected, principal: " + id);
    }
}
