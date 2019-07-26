/*
 * 7/26/19 8:09 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 8:01 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 8:01 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.controller.chatMessage;

import com.company.chess_online_bakend_api.controller.AbstractRestControllerTest;
import com.company.chess_online_bakend_api.controller.ChatMessageController;
import com.company.chess_online_bakend_api.controller.ExceptionAdviceController;
import com.company.chess_online_bakend_api.controller.RoomController;
import com.company.chess_online_bakend_api.exception.RoomNotFoundException;
import com.company.chess_online_bakend_api.service.jpa.ChatMessageServiceJpaImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChatMessageControllerTest extends AbstractRestControllerTest {

    @Mock
    private ChatMessageServiceJpaImpl chatMessageService;

    @InjectMocks
    private ChatMessageController chatMessageController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(chatMessageController)
                .setControllerAdvice(ExceptionAdviceController.class)
                .build();
    }


    @Test
    void getMessageCountInvalidRoomId() throws Exception {
        Long roomId = 1L;

        when(chatMessageService.getMessageCountForRoom(roomId)).thenThrow(RoomNotFoundException.class);

        mockMvc.perform(get(RoomController.BASE_URL + roomId + "/message/count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)));

        verify(chatMessageService, times(1)).getMessageCountForRoom(roomId);
        verifyNoMoreInteractions(chatMessageService);
    }

    @Test
    void getMessageCount() throws Exception {
        Long roomId = 1L;

        when(chatMessageService.getMessageCountForRoom(roomId)).thenReturn(10L);

        mockMvc.perform(get(RoomController.BASE_URL + roomId + "/message/count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", equalTo(10)));

        verify(chatMessageService, times(1)).getMessageCountForRoom(roomId);
        verifyNoMoreInteractions(chatMessageService);
    }
}