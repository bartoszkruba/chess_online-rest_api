package com.company.chess_online_bakend_api.controller.game;

import com.company.chess_online_bakend_api.controller.ExceptionAdviceController;
import com.company.chess_online_bakend_api.controller.GameController;
import com.company.chess_online_bakend_api.controller.propertyEditor.PieceColorPropertyEditor;
import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.exception.GameNotFoundException;
import com.company.chess_online_bakend_api.exception.PlaceAlreadyTakenException;
import com.company.chess_online_bakend_api.exception.UserNotFoundException;
import com.company.chess_online_bakend_api.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GameControllerTest {

    private final Long USER_COMMAND1_ID = 1L;
    private final Long USER_COMMAND2_ID = 2L;

    private final UserCommand USER_COMMAND1 = UserCommand.builder().id(USER_COMMAND1_ID).build();
    private final UserCommand USER_COMMAND2 = UserCommand.builder().id(USER_COMMAND2_ID).build();

    private final Long GAMECOMMAND1_ID = 1L;
    private final Long GAMECOMMAND2_ID = 2L;
    private final Long GAMECOMMAND3_ID = 3L;

    private final GameCommand GAMECOMMAND1 = GameCommand.builder()
            .id(GAMECOMMAND1_ID)
            .whitePlayer(USER_COMMAND1)
            .blackPlayer(USER_COMMAND2).build();

    private final GameCommand GAMECOMMAND2 = GameCommand.builder()
            .id(GAMECOMMAND2_ID)
            .whitePlayer(USER_COMMAND2)
            .build();

    private final GameCommand GAMECOMMAND3 = GameCommand.builder()
            .id(GAMECOMMAND3_ID)
            .blackPlayer(USER_COMMAND2)
            .build();

    @Mock
    PieceColorPropertyEditor pieceColorPropertyEditor;

    @Mock
    Principal principal;

    @Mock
    GameService gameService;

    @InjectMocks
    GameController gameController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(gameController)
                .setControllerAdvice(ExceptionAdviceController.class)
                .build();
    }

    @Test
    void getGameById() throws Exception {
        when(gameService.findById(GAMECOMMAND1_ID)).thenReturn(GAMECOMMAND1);

        mockMvc.perform(get(GameController.BASE_URL + GAMECOMMAND1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(GAMECOMMAND1_ID.intValue())))
                .andExpect(jsonPath("$.blackPlayer.id", equalTo(USER_COMMAND2_ID.intValue())))
                .andExpect(jsonPath("$.whitePlayer.id", equalTo(USER_COMMAND1_ID.intValue())));

        verify(gameService, times(1)).findById(GAMECOMMAND1_ID);
        verifyNoMoreInteractions(gameService);
    }

    @Test
    void getGameByIdNotFound() throws Exception {
        when(gameService.findById(GAMECOMMAND1_ID)).thenThrow(GameNotFoundException.class);

        mockMvc.perform(get(GameController.BASE_URL + GAMECOMMAND1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(gameService, times(1)).findById(GAMECOMMAND1_ID);
        verifyNoMoreInteractions(gameService);
    }

    @Test
    void joinGameUsernameNotFound() throws Exception {

        String username = "username";

        String url = GameController.BASE_URL + 1 + "/join/BLACK";

        when(principal.getName()).thenReturn(username);
        when(gameService.joinGame(any(), any(), anyLong())).thenThrow(UserNotFoundException.class);

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(principal))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)));

        verify(gameService, times(1)).joinGame(any(), any(), anyLong());
        verifyNoMoreInteractions(gameService);
    }

    @Test
    void joinGameGameNotFound() throws Exception {
        String username = "username";

        String url = GameController.BASE_URL + 1 + "/join/BLACK";

        when(principal.getName()).thenReturn(username);
        when(gameService.joinGame(any(), any(), anyLong())).thenThrow(GameNotFoundException.class);

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(principal))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)));

        verify(gameService, times(1)).joinGame(any(), any(), anyLong());
        verifyNoMoreInteractions(gameService);
    }

    @Test
    void joinGamePlaceAlreadyTaken() throws Exception {
        String username = "username";

        String url = GameController.BASE_URL + 1 + "/join/BLACK";

        when(principal.getName()).thenReturn(username);
        when(gameService.joinGame(any(), any(), anyLong())).thenThrow(PlaceAlreadyTakenException.class);

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(principal))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)));

        verify(gameService, times(1)).joinGame(any(), any(), anyLong());
        verifyNoMoreInteractions(gameService);
    }
}