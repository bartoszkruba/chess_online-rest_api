/*
 * 10/4/19, 5:00 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfiguration {

    public final static String DISCONNECT_QUEUE = "disconnectQueue";

    @Bean
    public Queue disconnectQueue() {
        return new Queue(DISCONNECT_QUEUE, false);
    }
}
