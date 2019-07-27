/*
 * 7/27/19 4:35 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service.socket;

import com.company.chess_online_bakend_api.data.model.ChatMessage;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.notification.ChatMessageNotification;
import com.company.chess_online_bakend_api.service.SocketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class SocketServiceImplTest {

    @Autowired
    SocketService socketService;

    @Value("${local.server.port}")
    private int port;
    private String URL;

    private CompletableFuture<ChatMessageNotification> completableFuture;

    @BeforeEach
    void setUp() {
        completableFuture = new CompletableFuture<>();
        URL = "ws://localhost:" + port + "/ws";
    }

    @Test
    void sendChatMessageNotification() throws InterruptedException, ExecutionException, TimeoutException {
        Long roomId = 1L;
        String username = "username";
        Long userId = 2l;
        String message = "message";

        var created = LocalDateTime.now();
        Long timestamp = created.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        var room = Room.builder().id(1L).build();
        var user = User.builder().id(userId).username(username).build();
        var chatMessage = ChatMessage.builder().room(room).user(user).message(message).created(created).build();

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe("/topic/room/" + roomId, new CreateGameStompFrameHandler());
        socketService.broadcastChatMessage(chatMessage);

        var chatMessageNotification = completableFuture.get(10, SECONDS);

        assertEquals(roomId, chatMessageNotification.getRoomId());
        assertEquals(username, chatMessageNotification.getUsername());
        assertEquals(userId, chatMessageNotification.getUserId());
        assertEquals(timestamp, chatMessageNotification.getTimestamp());
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private class CreateGameStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return ChatMessageNotification.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            completableFuture.complete((ChatMessageNotification) o);
        }
    }
}