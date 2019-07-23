package com.company.chess_online_bakend_api.controller.game;

import com.company.chess_online_bakend_api.controller.AbstractRestControllerTest;
import com.company.chess_online_bakend_api.controller.ExceptionAdviceController;
import com.company.chess_online_bakend_api.controller.GameController;
import com.company.chess_online_bakend_api.controller.propertyEditor.PieceColorPropertyEditor;
import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.command.MoveCommand;
import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.model.enums.PieceType;
import com.company.chess_online_bakend_api.exception.*;
import com.company.chess_online_bakend_api.service.GameService;
import com.company.chess_online_bakend_api.service.MoveService;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GameControllerTest extends AbstractRestControllerTest {

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

    @Mock
    MoveService moveService;


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

    @Test
    void leaveGameUsernameNotFound() throws Exception {
        String username = "username";

        String url = GameController.BASE_URL + 1 + "/leave";

        when(principal.getName()).thenReturn(username);
        when(gameService.leaveGame(username, 1L)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(principal))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)));

        verify(gameService, times(1)).leaveGame(username, 1L);
        verifyNoMoreInteractions(gameService);
    }

    @Test
    void leaveGameGameNotFound() throws Exception {
        String username = "username";

        String url = GameController.BASE_URL + 1 + "/leave";

        when(principal.getName()).thenReturn(username);
        when(gameService.leaveGame(username, 1L)).thenThrow(GameNotFoundException.class);

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(principal))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)));

        verify(gameService, times(1)).leaveGame(username, 1L);
        verifyNoMoreInteractions(gameService);
    }

    @Test
    void getValidMoves() throws Exception {
        MoveCommand moveCommand1 = MoveCommand.builder().from("A2").to("A3").build();
        MoveCommand moveCommand2 = MoveCommand.builder().from("A2").to("A4").build();

        MoveCommand request = MoveCommand.builder().from("A2").build();

        when(moveService.getPossibleMoves("A2", 1L)).thenReturn(Set.of(moveCommand1, moveCommand2));

        mockMvc.perform(get(GameController.BASE_URL + 1 + "/possibleMoves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    void performMove() throws Exception {
        String username = "username";
        Long gameId = 1L;
        String from = "D2";
        String to = "D3";

        LocalDateTime now = LocalDateTime.now();

        MoveCommand response = MoveCommand.builder()
                .from(from)
                .to(to)
                .count(1)
                .isKingAttacked(true)
                .happenedOn(now)
                .isCheckmate(true)
                .isDraw(true)
                .isKingSideCastle(true)
                .isQueenSideCastle(true)
                .pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.PAWN)
                .build();

        MoveCommand requestBody = MoveCommand.builder().from(from).to(to).build();

        when(principal.getName()).thenReturn(username);
        when(moveService.performMove(username, 1L, from, to)).thenReturn(response);

        mockMvc.perform(post(GameController.BASE_URL + gameId + "/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody))
                .principal(principal))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.from", equalTo(from)))
                .andExpect(jsonPath("$.to", equalTo(to)))
                .andExpect(jsonPath("$.count", equalTo(1)))
                .andExpect(jsonPath("$.isKingAttacked", equalTo(true)))
                .andExpect(jsonPath("$.isCheckmate", equalTo(true)))
                .andExpect(jsonPath("$.isDraw", equalTo(true)))
                .andExpect(jsonPath("$.isKingSideCastle", equalTo(true)))
                .andExpect(jsonPath("$.isQueenSideCastle", equalTo(true)))
                .andExpect(jsonPath("$.pieceColor", equalTo("WHITE")))
                .andExpect(jsonPath("$.pieceType", equalTo("PAWN")));

        verify(moveService, times(1)).performMove(username, gameId, from, to);
        verifyNoMoreInteractions(moveService);

        verifyZeroInteractions(gameService);
    }

    @Test
    void performInvalidMove() throws Exception {
        String username = "username";
        Long gameId = 1L;
        String from = "D2";
        String to = "D7";

        when(principal.getName()).thenReturn(username);
        when(moveService.performMove(username, 1L, from, to)).thenThrow(InvalidMoveException.class);

        MoveCommand requestBody = MoveCommand.builder().from(from).to(to).build();

        mockMvc.perform(post(GameController.BASE_URL + gameId + "/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody))
                .principal(principal))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)));

        verify(moveService, times(1)).performMove(username, gameId, from, to);
        verifyNoMoreInteractions(moveService);

        verifyZeroInteractions(gameService);
    }

    @Test
    void performMoveGameNotFound() throws Exception {
        String username = "username";
        Long gameId = 1L;
        String from = "D2";
        String to = "D7";

        when(principal.getName()).thenReturn(username);
        when(moveService.performMove(username, 1L, from, to)).thenThrow(GameNotFoundException.class);

        MoveCommand requestBody = MoveCommand.builder().from(from).to(to).build();

        mockMvc.perform(post(GameController.BASE_URL + gameId + "/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody))
                .principal(principal))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)));

        verify(moveService, times(1)).performMove(username, gameId, from, to);
        verifyNoMoreInteractions(moveService);

        verifyZeroInteractions(gameService);
    }

    @Test
    void performMoveUsernameNotFound() throws Exception {
        String username = "username";
        Long gameId = 1L;
        String from = "D2";
        String to = "D7";

        when(principal.getName()).thenReturn(username);
        when(moveService.performMove(username, 1L, from, to)).thenThrow(UserNotFoundException.class);

        MoveCommand requestBody = MoveCommand.builder().from(from).to(to).build();

        mockMvc.perform(post(GameController.BASE_URL + gameId + "/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody))
                .principal(principal))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)));

        verify(moveService, times(1)).performMove(username, gameId, from, to);
        verifyNoMoreInteractions(moveService);

        verifyZeroInteractions(gameService);
    }

    @Test
    void performForbiddenMove() throws Exception {
        String username = "username";
        Long gameId = 1L;
        String from = "D2";
        String to = "D7";

        when(principal.getName()).thenReturn(username);
        when(moveService.performMove(username, 1L, from, to)).thenThrow(ForbiddenException.class);

        MoveCommand requestBody = MoveCommand.builder().from(from).to(to).build();

        mockMvc.perform(post(GameController.BASE_URL + gameId + "/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody))
                .principal(principal))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", equalTo(403)));

        verify(moveService, times(1)).performMove(username, gameId, from, to);
        verifyNoMoreInteractions(moveService);

        verifyZeroInteractions(gameService);
    }

    @Test
    void PerformMoveBrokenGameLogic() throws Exception {
        String username = "username";
        Long gameId = 1L;
        String from = "D2";
        String to = "D7";

        when(principal.getName()).thenReturn(username);
        when(moveService.performMove(username, 1L, from, to)).thenThrow(MoveGeneratorException.class);

        MoveCommand requestBody = MoveCommand.builder().from(from).to(to).build();

        mockMvc.perform(post(GameController.BASE_URL + gameId + "/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody))
                .principal(principal))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status", equalTo(500)));

        verify(moveService, times(1)).performMove(username, gameId, from, to);
        verifyNoMoreInteractions(moveService);

        verifyZeroInteractions(gameService);
    }
}