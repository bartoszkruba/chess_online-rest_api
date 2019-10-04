/*
 * 10/4/19, 4:53 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import static com.company.chess_online_bakend_api.amqp.QueueConfiguration.DISCONNECT_QUEUE;

@Configuration
public class DisconnectListener {

    @RabbitListener(queues = DISCONNECT_QUEUE)
    public void listen(String disconnectMessage) {
        System.out.println("Received new message: ");
        System.out.println(disconnectMessage);
    }
}
