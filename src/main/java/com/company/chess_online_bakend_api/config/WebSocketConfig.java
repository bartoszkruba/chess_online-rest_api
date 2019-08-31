/*
 * 7/27/19 4:11 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.config;

import com.company.chess_online_bakend_api.data.model.WebSocketId;
import com.company.chess_online_bakend_api.data.repository.WebSocketIdRepository;
import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;

    private final WebSocketIdRepository webSocketIdRepository;

    @Autowired
    public WebSocketConfig(WebSocketIdRepository webSocketIdRepository) {
        this.webSocketIdRepository = webSocketIdRepository;
    }

    @Setter
    @Builder
    private static class CustomPrincipal implements Principal {

        private String name;

        @Override
        public String getName() {
            return name;
        }
    }

    private class CustomHandshakeHandler extends DefaultHandshakeHandler {
        @Override
        protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                          Map<String, Object> attributes) {
            String uniqueId = UUID.randomUUID().toString();

            Optional<WebSocketId> optionalWebSocketId = webSocketIdRepository.findById(uniqueId);

            if (optionalWebSocketId.isEmpty()) {
                return CustomPrincipal.builder().name(uniqueId).build();
            } else {
                log.debug("Generating unique id for web socket connection: " + uniqueId);
                return this.determineUser(request, wsHandler, attributes);
            }
        }
    }

    public HandshakeHandler customHandshakeHandler() {
        return new CustomHandshakeHandler();
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws").setAllowedOrigins("*")
                .setHandshakeHandler(customHandshakeHandler())
                .withSockJS();
        registry.addEndpoint("/ws").setAllowedOrigins("*")
                .setHandshakeHandler(customHandshakeHandler());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        log.info("Connecting to RabbitMQ");

        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost(host)
                .setRelayPort(61613)
                .setClientLogin(username)
                .setClientPasscode(password);

        registry.setApplicationDestinationPrefixes("/ws");
    }
}
