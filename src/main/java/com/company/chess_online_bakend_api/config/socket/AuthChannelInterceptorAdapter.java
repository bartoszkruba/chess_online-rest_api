/*
 * 8/31/19, 7:29 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.config.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {
    private final String USERNAME_HEADER = "login";
    private final String PASSWORD_HEADER = "password";

    private final WebSocketAuthenticatorService webSocketAuthenticatorService;

    @Autowired
    public AuthChannelInterceptorAdapter(WebSocketAuthenticatorService webSocketAuthenticatorService) {
        this.webSocketAuthenticatorService = webSocketAuthenticatorService;
    }


    // TODO: 2019-09-02 Figure out how to send error to client if authentication fails
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.debug("Intercepting new message, " + message.getPayload());
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            log.error("accessor is null");
            return null;
        } else if (accessor.getCommand() == StompCommand.CONNECT) {
            final String username = accessor.getFirstNativeHeader(USERNAME_HEADER);
            final String password = accessor.getFirstNativeHeader(PASSWORD_HEADER);

            final UsernamePasswordAuthenticationToken user =
                    webSocketAuthenticatorService.getAuthenticated(username, password);

            accessor.setUser(user);
        } else if (accessor.getCommand() == StompCommand.SEND) {
            if (accessor.getUser() == null || accessor.getUser().getName().isEmpty()) {
                log.debug("User not authorized - preventing message");
                return null;
            }
        }

        return message;
    }

}
