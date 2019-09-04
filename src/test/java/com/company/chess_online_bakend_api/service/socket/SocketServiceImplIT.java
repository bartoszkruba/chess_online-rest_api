/*
 * 7/27/19 4:35 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service.socket;

import com.company.chess_online_bakend_api.bootstrap.dev.DeleteSessionsBootstrap;
import com.company.chess_online_bakend_api.bootstrap.dev.RoomBootstrap;
import com.company.chess_online_bakend_api.bootstrap.dev.UserBootstrap;
import com.company.chess_online_bakend_api.controller.GameController;
import com.company.chess_online_bakend_api.data.model.ChatMessage;
import com.company.chess_online_bakend_api.data.model.Move;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.model.enums.HorizontalPosition;
import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.model.enums.PieceType;
import com.company.chess_online_bakend_api.data.model.enums.VerticalPosition;
import com.company.chess_online_bakend_api.data.notification.ChatMessageNotification;
import com.company.chess_online_bakend_api.data.notification.enums.GameOverCause;
import com.company.chess_online_bakend_api.data.notification.enums.NotificationType;
import com.company.chess_online_bakend_api.data.repository.GameRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import io.github.artsok.RepeatedIfExceptionsTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.WebSocketHttpHeaders;
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
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// TODO: 2019-08-24 Sometimes socket tests fail due to timeout exception - find out why
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class SocketServiceImplIT {

    @Autowired
    private SocketService socketService;

    @Autowired
    private DeleteSessionsBootstrap deleteSessionsBootstrap;
    @Autowired
    private RoomBootstrap roomBootstrap;
    @Autowired
    private UserBootstrap userBootstrap;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Value("${local.server.port}")
    private int port;
    private String URL;

    private CompletableFuture<Object> completableFuture;

    @BeforeEach
    void setUp() throws Exception {
        deleteSessionsBootstrap.run();
        roomBootstrap.run();
        userBootstrap.onApplicationEvent(null);

        completableFuture = new CompletableFuture<>();
        URL = "ws://localhost:" + port + "/chess";

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        deleteSessionsBootstrap.run();
        roomRepository.deleteAll();
        userRepository.deleteAll();
    }

    @RepeatedIfExceptionsTest(repeats = 3)
    void sendChatMessageNotification() throws InterruptedException, ExecutionException, TimeoutException {
        Long roomId = 1L;
        String username = "username";
        Long userId = 2L;
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

        stompSession.subscribe("/topic/room." + roomId,
                new CreateNotificationStompFrameHandler(ChatMessageNotification.class));

        socketService.broadcastChatMessage(chatMessage);

        var notification = (ChatMessageNotification) completableFuture.get(10, SECONDS);

        assertEquals(roomId, notification.getRoomId());
        assertEquals(username, notification.getUsername());
        assertEquals(userId, notification.getUserId());
        assertEquals(timestamp, notification.getTimestamp());
        assertEquals(NotificationType.CHAT_MESSAGE, notification.getNotificationType());

        stompSession.disconnect();
    }

    @RepeatedIfExceptionsTest(repeats = 3)
    void sendJoinGameNotification() throws InterruptedException, ExecutionException, TimeoutException {
        Long roomId = 1L;
        Long userId = 2L;
        Long gameId = 3L;
        String username = "username";
        var color = PieceColor.BLACK;
        String fenNotation = "notation";

        var user = User.builder().id(userId).username(username).build();

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe("/topic/room." + roomId,
                new CreateNotificationStompFrameHandler(Object.class));

        socketService.broadcastJoinGame(user, gameId, color, fenNotation, roomId);

        var notification = (Map) completableFuture.get(10, SECONDS);

        assertEquals(userId.intValue(), ((Map) notification.get("user")).get("id"));
        assertEquals(username, ((Map) notification.get("user")).get("username"));
        assertEquals(gameId.intValue(), notification.get("gameId"));
        assertEquals(color.toString(), notification.get("color"));
        assertEquals(fenNotation, notification.get("fenNotation"));
        assertEquals(NotificationType.JOIN_GAME.toString(), notification.get("notificationType"));

        stompSession.disconnect();
    }

    @RepeatedIfExceptionsTest(repeats = 3)
    void sendLeaveGameNotification() throws InterruptedException, ExecutionException, TimeoutException {
        Long roomId = 1L;
        Long userId = 2L;
        Long gameId = 3L;
        String username = "username";
        var color = PieceColor.BLACK;
        String fenNotation = "notation";

        var user = User.builder().id(userId).username(username).build();

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe("/topic/room." + roomId,
                new CreateNotificationStompFrameHandler(Object.class));

        socketService.broadcastLeaveGame(user, gameId, color, fenNotation, roomId);

        var notification = (Map) completableFuture.get(10, SECONDS);

        assertEquals(userId.intValue(), ((Map) notification.get("user")).get("id"));
        assertEquals(username, ((Map) notification.get("user")).get("username"));
        assertEquals(gameId.intValue(), notification.get("gameId"));
        assertEquals(color.toString(), notification.get("color"));
        assertEquals(fenNotation, notification.get("fenNotation"));
        assertEquals(NotificationType.LEAVE_GAME.toString(), notification.get("notificationType"));

        stompSession.disconnect();
    }

    @RepeatedIfExceptionsTest(repeats = 3)
    void sendMoveNotification() throws InterruptedException, ExecutionException, TimeoutException {
        Long roomId = 1L;
        var color = PieceColor.WHITE;
        var time = LocalDateTime.now();
        Long timestamp = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        var hStart = HorizontalPosition.A;
        var vStart = VerticalPosition.ONE;

        var hEnd = HorizontalPosition.B;
        var vEnd = VerticalPosition.TWO;

        String startPosition = "A1";
        String endPosition = "B2";

        var pieceType = PieceType.KING;

        Boolean isKingSideCastle = true;
        Boolean isQueenSideCastle = true;
        Boolean isKingAttacked = true;
        Boolean isCheckmate = true;
        Boolean isDraw = true;
        int moveCount = 1;

        String fenNotation = "notation";
        Long gameId = 1L;

        var move = Move.builder()
                .pieceColor(color)
                .created(time)
                .horizontalStartPosition(hStart)
                .verticalStartPosition(vStart)
                .horizontalEndPosition(hEnd)
                .verticalEndPosition(vEnd)
                .pieceType(pieceType)
                .isQueenSideCastle(isQueenSideCastle)
                .isKingSideCastle(isKingSideCastle)
                .isKingAttacked(isKingAttacked)
                .isCheckmate(isCheckmate)
                .isDraw(isDraw)
                .moveCount(moveCount).build();

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe("/topic/room." + roomId,
                new CreateNotificationStompFrameHandler(Object.class));

        socketService.broadcastMove(move, fenNotation, gameId, roomId);

        var notification = (Map) completableFuture.get(10, SECONDS);

        assertEquals(NotificationType.MOVE.toString(), notification.get("notificationType"));
        assertEquals(color.toString(), notification.get("color").toString());
        assertEquals(timestamp, notification.get("timestamp"));
        assertEquals(startPosition, notification.get("from"));
        assertEquals(endPosition, notification.get("to"));
        assertEquals(pieceType.toString(), notification.get("pieceType"));
        assertEquals(isQueenSideCastle, notification.get("isQueenSideCastle"));
        assertEquals(isKingSideCastle, notification.get("isKingSideCastle"));
        assertEquals(isKingAttacked, notification.get("isKingAttacked"));
        assertEquals(isCheckmate, notification.get("isCheckmate"));
        assertEquals(isDraw, notification.get("isDraw"));
        assertEquals(moveCount, notification.get("moveCount"));
        assertEquals(fenNotation, notification.get("fenNotation"));
        assertEquals(gameId.intValue(), notification.get("gameId"));

        stompSession.disconnect();
    }

    @RepeatedIfExceptionsTest(repeats = 3)
    void sendGameOverDraw() throws InterruptedException, ExecutionException, TimeoutException {
        Long gameId = 1L;
        Long roomId = 2L;
        String fenNotation = "notation";

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe("/topic/room." + roomId,
                new CreateNotificationStompFrameHandler(Object.class));

        socketService.broadcastGameOverWithDraw(fenNotation, gameId, roomId);

        var notification = (Map) completableFuture.get(10, SECONDS);

        assertEquals(GameOverCause.DRAW.toString(), notification.get("gameOverCause"));
        assertEquals(fenNotation, notification.get("fenNotation"));
        assertEquals(gameId.intValue(), notification.get("gameId"));

        stompSession.disconnect();
    }

    @RepeatedIfExceptionsTest(repeats = 3)
    void sendGameOverCheckmate() throws InterruptedException, ExecutionException, TimeoutException {
        Long gameId = 1L;
        Long roomId = 2L;
        String fenNotation = "notation";

        String username = "username";
        Long userId = 1L;

        var winnerColor = PieceColor.WHITE;

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe("/topic/room." + roomId,
                new CreateNotificationStompFrameHandler(Object.class));

        socketService.broadcastGameOverWithCheckmate(User.builder().username(username).id(userId).build(),
                winnerColor, fenNotation, gameId, roomId);

        var notification = (Map) completableFuture.get(10, SECONDS);

        assertEquals(GameOverCause.CHECKMATE.toString(), notification.get("gameOverCause"));
        assertEquals(fenNotation, notification.get("fenNotation"));
        assertEquals(gameId.intValue(), notification.get("gameId"));
        assertEquals(winnerColor.toString(), notification.get("winnerColor"));
        assertEquals(username, ((Map) notification.get("winner")).get("username"));
        assertEquals(userId.intValue(), ((Map) notification.get("winner")).get("id"));

        stompSession.disconnect();
    }

    @RepeatedIfExceptionsTest(repeats = 3)
    void sendGameOverPlayerLeft() throws InterruptedException, ExecutionException, TimeoutException {
        Long gameId = 1L;
        Long roomId = 2L;
        String fenNotation = "notation";

        String username = "username";
        Long userId = 1L;

        var winnerColor = PieceColor.WHITE;

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe("/topic/room." + roomId,
                new CreateNotificationStompFrameHandler(Object.class));

        socketService.broadcastGameOverWithPlayerLeft(User.builder().username(username).id(userId).build(),
                winnerColor, fenNotation, gameId, roomId);

        var notification = (Map) completableFuture.get(10, SECONDS);

        assertEquals(GameOverCause.PLAYER_LEFT.toString(), notification.get("gameOverCause"));
        assertEquals(fenNotation, notification.get("fenNotation"));
        assertEquals(gameId.intValue(), notification.get("gameId"));
        assertEquals(winnerColor.toString(), notification.get("winnerColor"));
        assertEquals(username, ((Map) notification.get("winner")).get("username"));
        assertEquals(userId.intValue(), ((Map) notification.get("winner")).get("id"));

        stompSession.disconnect();
    }

    @Test
    @WithMockUser(username = UserBootstrap.USER_USERNAME, authorities = {UserBootstrap.ROLE_USER})
    void pingWhiteNotAuthorized() throws Exception {
        StompHeaders connectHeaders = new StompHeaders();

        var game = roomRepository.findByNameLike("Room 1").get().getGame();
        Long gameId = game.getId();

        mockMvc.perform(put(GameController.BASE_URL + gameId + "/join/white")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new WebSocketHttpHeaders(), connectHeaders,
                new StompSessionHandlerAdapter() {
                }).get(5, SECONDS);

        String address = "/app/chess/game." + gameId + ".ping";

        stompSession.send(address, null);

        // Waiting for service to update game in DB
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        var updatedGame = gameRepository.findById(gameId).get();

        assertNull(updatedGame.getWhitePing());
    }

    @Test
    @WithMockUser(username = UserBootstrap.USER_USERNAME, authorities = {UserBootstrap.ROLE_USER})
    void pingWhite() throws Exception {
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("login", UserBootstrap.USER_USERNAME);
        connectHeaders.add("password", UserBootstrap.USER_PASSWORD);

        var game = roomRepository.findByNameLike("Room 1").get().getGame();
        Long gameId = game.getId();

        mockMvc.perform(put(GameController.BASE_URL + gameId + "/join/white")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new WebSocketHttpHeaders(), connectHeaders,
                new StompSessionHandlerAdapter() {
                }).get(5, SECONDS);

        String address = "/app/chess/game." + gameId + ".ping";

        stompSession.send(address, null);

        // Waiting for service to update game in DB
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        var updatedGame = gameRepository.findById(gameId).get();

        assertNotNull(updatedGame.getWhitePing());
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }


    private class CreateNotificationStompFrameHandler implements StompFrameHandler {

        private Type clazz;

        public CreateNotificationStompFrameHandler(Type clazz) {
            this.clazz = clazz;
        }

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return clazz;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            completableFuture.complete(o);
        }
    }
}