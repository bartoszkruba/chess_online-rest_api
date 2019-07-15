package com.company.chess_online_bakend_api.controller.room;

import com.company.chess_online_bakend_api.controller.AbstractRestControllerTest;
import com.company.chess_online_bakend_api.controller.ExceptionAdviceController;
import com.company.chess_online_bakend_api.controller.RoomController;
import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.command.RoomCommand;
import com.company.chess_online_bakend_api.exception.RoomNotFoundException;
import com.company.chess_online_bakend_api.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoomControllerTest extends AbstractRestControllerTest {

    private final Long GAMECOMMAND1_ID = 1L;
    private final Long GAMECOMMAND2_ID = 2L;

    private final Long ROOMCOMMAND1_ID = 3L;
    private final Long ROOMCOMMAND2_ID = 4L;

    private final String ROOMCOMMAND1_NAME = "Room Name 1";
    private final String ROOMCOMMAND2_NAME = "Room Name 2";

    private final GameCommand GAMECOMMAND1 = GameCommand.builder().id(GAMECOMMAND1_ID).build();
    private final GameCommand GAMECOMMAND2 = GameCommand.builder().id(GAMECOMMAND2_ID).build();

    private final RoomCommand ROOMCOMMAND1 = RoomCommand.builder().id(ROOMCOMMAND1_ID)
            .name(ROOMCOMMAND1_NAME)
            .game(GAMECOMMAND1).build();
    private final RoomCommand ROOMCOMMAND2 = RoomCommand.builder().id(ROOMCOMMAND2_ID)
            .name(ROOMCOMMAND2_NAME)
            .game(GAMECOMMAND2).build();

    private final RoomCommand ROOMCOMMAND1_WITHOUT_GAME = RoomCommand.builder().id(ROOMCOMMAND1_ID)
            .name(ROOMCOMMAND1_NAME).build();
    private final RoomCommand ROOMCOMMAND2_WITHOUT_GAME = RoomCommand.builder().id(ROOMCOMMAND2_ID)
            .name(ROOMCOMMAND2_NAME).build();

    private final int ROOM_COUNT = 2;
    private final int ROOM_PAGE = 0;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(roomController)
                .setControllerAdvice(ExceptionAdviceController.class)
                .build();

        when(roomService.findById(ROOMCOMMAND1_ID)).thenReturn(ROOMCOMMAND1);
        when(roomService.findById(ROOMCOMMAND2_ID)).thenReturn(ROOMCOMMAND2);

        when(roomService.getRoomCount()).thenReturn((long) ROOM_COUNT);

        when(roomService.getRoomPage(ROOM_PAGE))
                .thenReturn(Set.of(ROOMCOMMAND1_WITHOUT_GAME, ROOMCOMMAND2_WITHOUT_GAME));

    }

    @Test
    void getRoomById() throws Exception {

        mockMvc.perform(get(RoomController.BASE_URL + ROOMCOMMAND1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(ROOMCOMMAND1_ID.intValue())))
                .andExpect(jsonPath("$.name", equalTo(ROOMCOMMAND1_NAME)))
                .andExpect(jsonPath("$.game.id", equalTo(GAMECOMMAND1_ID.intValue())));

        verify(roomService, times(1)).findById(ROOMCOMMAND1_ID);
        verifyNoMoreInteractions(roomService);
    }

    @Test
    void getRoomByIdNotFound() throws Exception {

        when(roomService.findById(anyLong())).thenThrow(RoomNotFoundException.class);

        mockMvc.perform(get(RoomController.BASE_URL + "2439")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(roomService, times(1)).findById(anyLong());
        verifyNoMoreInteractions(roomService);
    }

    @Test
    void getRoomCount() throws Exception {
        mockMvc.perform(get(RoomController.BASE_URL + "count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", equalTo(ROOM_COUNT)));

        verify(roomService, times(1)).getRoomCount();
        verifyNoMoreInteractions(roomService);
    }

    @Test
    void getRoomPage() throws Exception {
        mockMvc.perform(get(RoomController.BASE_URL + "page/" + ROOM_PAGE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].game").doesNotExist())
                .andExpect(jsonPath("$[1].game").doesNotExist());;

        verify(roomService, times(1)).getRoomPage(ROOM_PAGE);
        verifyNoMoreInteractions(roomService);
    }
}