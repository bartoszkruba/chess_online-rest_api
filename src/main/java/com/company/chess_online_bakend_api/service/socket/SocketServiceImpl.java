/*
 * 7/27/19 3:53 PM. Updated by Bartosz Kruba.
 */

/*
 * 7/27/19 3:53 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service.socket;

import com.company.chess_online_bakend_api.data.model.ChatMessage;
import com.company.chess_online_bakend_api.service.SocketService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SocketServiceImpl implements SocketService {

    @Override
    @Async
    public void notifyChatMessage(ChatMessage chatMessage) {

    }
}
