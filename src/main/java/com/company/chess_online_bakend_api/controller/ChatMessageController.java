/*
 * 7/26/19 8:09 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:53 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.controller;

import com.company.chess_online_bakend_api.data.command.ChatMessageCommand;
import com.company.chess_online_bakend_api.data.command.CountCommand;
import com.company.chess_online_bakend_api.service.ChatMessageService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @Autowired
    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @ApiOperation(value = "Get total message count for room.",
            notes = "Returns 404 NOT FOUND if room does not exist.")
    @GetMapping({RoomController.BASE_URL + "{id}/message/count", RoomController.BASE_URL + "{id}/message/count/"})
    @ResponseStatus(HttpStatus.OK)
    public CountCommand getMessageCount(@PathVariable Long id) {
        log.debug("New request: GET " + RoomController.BASE_URL + id + "/message/count");

        return CountCommand.builder().count(chatMessageService.getMessageCountForRoom(id)).build();
    }

    @ApiOperation(value = "Get message page for room.",
            notes = "Returns 404 NOt FOUND if room does not exist. \n " +
                    "10 messages per page.")
    @GetMapping({RoomController.BASE_URL + "{id}/message/page/{page}",
            RoomController.BASE_URL + "{id}/message/page/{page}/"})
    @ResponseStatus(HttpStatus.OK)
    public List<ChatMessageCommand> getMessagePageForRoom(@PathVariable Long id, @PathVariable int page) {
        log.debug("New request: Get " + RoomController.BASE_URL + id + "/message/page/" + page);

        return chatMessageService.getMessagePageForRoom(id, page);
    }
}
