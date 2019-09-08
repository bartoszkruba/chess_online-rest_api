/*
 * 9/1/19, 5:05 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.config.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketAuthorizationSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.simpDestMatchers("/app/chess/**").authenticated();

    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
