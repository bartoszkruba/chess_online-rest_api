/*
 * 7/27/19 3:53 PM. Updated by Bartosz Kruba.
 */

/*
 * 7/27/19 3:53 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service.socket;

import com.company.chess_online_bakend_api.data.converter.notification.chatMessage.ChatMessageToChatNotification;
import com.company.chess_online_bakend_api.data.model.ChatMessage;
import com.company.chess_online_bakend_api.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SocketServiceImpl implements SocketService {

    private final ChatMessageToChatNotification chatMessageToChatNotification;

    private final SimpMessageSendingOperations messagingTemplate;

    public SocketServiceImpl(ChatMessageToChatNotification chatMessageToChatNotification,
                             SimpMessageSendingOperations messagingTemplate) {
        this.chatMessageToChatNotification = chatMessageToChatNotification;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @Async
    public void broadcastChatMessage(ChatMessage chatMessage) {
        log.debug("Broadcasting ChatMessage to room " + chatMessage.getRoom().getId());

        String channel = "/topic/room/" + chatMessage.getRoom().getId();

        messagingTemplate.convertAndSend(channel, chatMessageToChatNotification.convert(chatMessage));
    }
}
