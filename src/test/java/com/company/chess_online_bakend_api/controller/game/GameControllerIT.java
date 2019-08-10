/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.controller.game;

import com.company.chess_online_bakend_api.bootstrap.dev.RoomBootstrap;
import com.company.chess_online_bakend_api.bootstrap.dev.UserBootstrap;
import com.company.chess_online_bakend_api.controller.AbstractRestControllerTest;
import com.company.chess_online_bakend_api.controller.GameController;
import com.company.chess_online_bakend_api.data.command.MoveCommand;
import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.GameRepository;
import com.company.chess_online_bakend_api.data.repository.RoleRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.data.validation.constraint.ValidPositionConstraint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class GameControllerIT extends AbstractRestControllerTest {

    MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserBootstrap userBootstrap;

    @Autowired
    private RoomBootstrap roomBootstrap;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setup() throws Exception {
        userBootstrap.onApplicationEvent(null);
        roomBootstrap.run();

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() {
        roomRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = UserBootstrap.ADMIN_USERNAME, authorities = UserBootstrap.ROLE_ADMIN)
    void joinGameAsAdmin() throws Exception {

        Long userId = userRepository.findByUsernameLike(UserBootstrap.ADMIN_USERNAME).get().getId();
        Long gameId = roomRepository.findByNameLike("Alpha").get().getGame().getId();

        String url = GameController.BASE_URL + gameId + "/join/white";

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(gameId.intValue())))
                .andExpect(jsonPath("$.whitePlayer.id", equalTo(userId.intValue())));
    }

    @Test
    @WithMockUser(username = UserBootstrap.USER_USERNAME, authorities = UserBootstrap.ROLE_USER)
    void joinGameAsUser() throws Exception {

        Long userId = userRepository.findByUsernameLike(UserBootstrap.USER_USERNAME).get().getId();
        Long gameId = roomRepository.findByNameLike("Alpha").get().getGame().getId();

        String url = GameController.BASE_URL + gameId + "/join/BLACK";

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(gameId.intValue())))
                .andExpect(jsonPath("$.blackPlayer.id", equalTo(userId.intValue())));
    }

    @Test
    void joinGameNotLogged() throws Exception {
        String url = GameController.BASE_URL + 1 + "/join/BLACK";

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = UserBootstrap.USER_USERNAME, authorities = UserBootstrap.ROLE_USER)
    void joinGameInvalidColor() throws Exception {
        String url = GameController.BASE_URL + 13 + "/join/orange";

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "invalid username", authorities = UserBootstrap.ROLE_USER)
    void joinGameUsernameNotFound() throws Exception {
        Long userId = userRepository.findByUsernameLike(UserBootstrap.USER_USERNAME).get().getId();
        Long gameId = roomRepository.findByNameLike("Alpha").get().getGame().getId();

        String url = GameController.BASE_URL + gameId + "/join/BLACK";

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)));
    }

    @Test
    void leaveGameNotLogged() throws Exception {
        mockMvc.perform(put(GameController.BASE_URL + 12 + "/leave")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @WithMockUser(username = UserBootstrap.USER_USERNAME, authorities = UserBootstrap.ROLE_USER)
    void leaveGameLoggedAsUser() throws Exception {

        User user = userRepository.findByUsernameLike(UserBootstrap.USER_USERNAME).get();
        Room room = roomRepository.findByNameLike("Alpha").get();
        Game game = room.getGame();
        Long gameId = game.getId();

        game.setWhitePlayer(user);

        gameRepository.save(game);

        String url = GameController.BASE_URL + gameId + "/leave";

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(gameId.intValue())))
                .andExpect(jsonPath("$.whitePlayer").doesNotExist());
    }

    @Test
    @Transactional
    @WithMockUser(username = UserBootstrap.ADMIN_USERNAME, authorities = UserBootstrap.ROLE_ADMIN)
    void leaveGameLoggedAsAdmin() throws Exception {
        User user = userRepository.findByUsernameLike(UserBootstrap.ADMIN_USERNAME).get();
        Room room = roomRepository.findByNameLike("Alpha").get();
        Game game = room.getGame();
        Long gameId = game.getId();

        game.setWhitePlayer(user);


        gameRepository.save(game);

        String url = GameController.BASE_URL + gameId + "/leave";

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(gameId.intValue())))
                .andExpect(jsonPath("$.whitePlayer").doesNotExist());
    }

    @Test
    @WithMockUser(username = "Invalid username", authorities = UserBootstrap.ROLE_ADMIN)
    void leaveGameUsernameNotFound() throws Exception {
        mockMvc.perform(put(GameController.BASE_URL + 24390 + "/leave")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)));
    }

    @Test
    void getPossibleMovesInvalidPosition() throws Exception {
        MoveCommand moveCommand = MoveCommand.builder().from("L").build();

        mockMvc.perform(get(GameController.BASE_URL + 13 + "/possibleMoves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(moveCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.from", hasSize(1)))
                .andExpect(jsonPath("$.errors.from[0]", equalTo(ValidPositionConstraint.ERROR_MESSAGE)))
                .andExpect(jsonPath("$.status", equalTo(400)));
    }

    @Test
    void performMoveUnauthorized() throws Exception {
        Long gameId = 1L;
        String from = "D2";
        String to = "D3";

        MoveCommand requestBody = MoveCommand.builder().from(from).to(to).build();

        mockMvc.perform(post(GameController.BASE_URL + gameId + "/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void performMoveNullValues() throws Exception {
        Long gameId = 1L;

        MoveCommand requestBody = MoveCommand.builder().build();

        mockMvc.perform(post(GameController.BASE_URL + gameId + "/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.from[0]", equalTo(MoveCommand.MESSAGE_FROM_NULL)))
                .andExpect(jsonPath("$.errors.from", hasSize(1)))
                .andExpect(jsonPath("$.errors.to[0]", equalTo(MoveCommand.MESSAGE_TO_NULL)))
                .andExpect(jsonPath("$.errors.to", hasSize(1)));
    }

    @Test
    void performMoveInvalidPositions() throws Exception {
        Long gameId = 1L;

        String from = "z";
        String to = "x";

        MoveCommand requestBody = MoveCommand.builder().from(from).to(to).build();

        mockMvc.perform(post(GameController.BASE_URL + gameId + "/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.from[0]", equalTo(ValidPositionConstraint.ERROR_MESSAGE)))
                .andExpect(jsonPath("$.errors.from", hasSize(1)))
                .andExpect(jsonPath("$.errors.to[0]", equalTo(ValidPositionConstraint.ERROR_MESSAGE)))
                .andExpect(jsonPath("$.errors.to", hasSize(1)));
    }

    @Test
    @WithMockUser(username = UserBootstrap.USER_USERNAME)
    void performMoveAsUser() throws Exception {
        Long gameId = roomRepository.findByNameLike("Alpha").get().getGame().getId();

        String from = "d2";
        String to = "d3";

        MoveCommand requestBody = MoveCommand.builder().from(from).to(to).build();
        User user = userRepository.findByUsernameLike(UserBootstrap.USER_USERNAME).get();
        User admin = userRepository.findByUsernameLike(UserBootstrap.ADMIN_USERNAME).get();

        Game game = gameRepository.findById(gameId).get();

        game.setWhitePlayer(user);
        game.setBlackPlayer(admin);

        gameRepository.save(game);

        mockMvc.perform(post(GameController.BASE_URL + gameId + "/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pieceType", equalTo("PAWN")))
                .andExpect(jsonPath("$.pieceColor", equalTo("WHITE")))
                .andExpect(jsonPath("$.from", equalTo("D2")))
                .andExpect(jsonPath("$.to", equalTo("D3")));
    }

    @Test
    @WithMockUser(username = UserBootstrap.ADMIN_USERNAME)
    void performMoveAsAdmin() throws Exception {
        Long gameId = roomRepository.findByNameLike("Alpha").get().getGame().getId();

        String from = "d2";
        String to = "d3";

        MoveCommand requestBody = MoveCommand.builder().from(from).to(to).build();
        User user = userRepository.findByUsernameLike(UserBootstrap.USER_USERNAME).get();
        User admin = userRepository.findByUsernameLike(UserBootstrap.ADMIN_USERNAME).get();

        Game game = gameRepository.findById(gameId).get();

        game.setWhitePlayer(admin);
        game.setBlackPlayer(user);

        gameRepository.save(game);

        mockMvc.perform(post(GameController.BASE_URL + gameId + "/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pieceType", equalTo("PAWN")))
                .andExpect(jsonPath("$.pieceColor", equalTo("WHITE")))
                .andExpect(jsonPath("$.from", equalTo("D2")))
                .andExpect(jsonPath("$.to", equalTo("D3")));
    }
}
