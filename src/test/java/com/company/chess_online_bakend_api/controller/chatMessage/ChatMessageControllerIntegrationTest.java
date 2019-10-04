/*
 * 7/26/19 8:38 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.controller.chatMessage;

import com.company.chess_online_bakend_api.bootstrap.dev.DeleteSessionsBootstrap;
import com.company.chess_online_bakend_api.bootstrap.dev.UserBootstrap;
import com.company.chess_online_bakend_api.controller.AbstractRestControllerTest;
import com.company.chess_online_bakend_api.controller.RoomController;
import com.company.chess_online_bakend_api.data.command.ChatMessageCommand;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
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
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class ChatMessageControllerIntegrationTest extends AbstractRestControllerTest {

    @Autowired
    private DeleteSessionsBootstrap deleteSessionsBootstrap;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roomRepository.deleteAll();

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        userRepository.deleteAll();
        roomRepository.deleteAll();
        deleteSessionsBootstrap.run();
    }

    @Test
    void createNewMessageNotLogged() throws Exception {
        var chatMessageCommand = ChatMessageCommand.builder().message("ddddd sdadasads dasdasads").build();

        mockMvc.perform(post(RoomController.BASE_URL + "1/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chatMessageCommand)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = UserBootstrap.ADMIN_USERNAME, authorities = UserBootstrap.ROLE_ADMIN)
    void createNewMessageLoggedAsAdmin() throws Exception {
        var chatMessageCommand = ChatMessageCommand.builder().message("ddddd sdadasads dasdasads").build();

        mockMvc.perform(post(RoomController.BASE_URL + "1/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chatMessageCommand)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)));
    }

    @Test
    @WithMockUser(username = UserBootstrap.USER_USERNAME, authorities = UserBootstrap.ROLE_USER)
    void createNewMessageLoggedAsUser() throws Exception {
        var chatMessageCommand = ChatMessageCommand.builder().message("ddddd sdadasads dasdasads").build();

        mockMvc.perform(post(RoomController.BASE_URL + "1/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chatMessageCommand)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)));
    }

    @Test
    void createNewMessageNullMessage() throws Exception {
        var chatMessageCommand = ChatMessageCommand.builder().message(null).build();

        mockMvc.perform(post(RoomController.BASE_URL + "1/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chatMessageCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.errors.message[0]", equalTo(ChatMessageCommand.MESSAGE_NOT_BLANK_ERROR)))
                .andExpect(jsonPath("$.errors.message", hasSize(1)));
    }

    @Test
    void createNewMessageBlankMessage() throws Exception {
        var chatMessageCommand = ChatMessageCommand.builder().message("      ").build();

        mockMvc.perform(post(RoomController.BASE_URL + "1/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chatMessageCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.errors.message[0]", equalTo(ChatMessageCommand.MESSAGE_NOT_BLANK_ERROR)))
                .andExpect(jsonPath("$.errors.message", hasSize(1)));
    }

    @Test
    void createNewMessageInvalidLength() throws Exception {
        var sb = new StringBuilder();

        for (int i = 0; i < 501; i++) {
            sb.append("b");
        }

        var chatMessageCommand = ChatMessageCommand.builder()
                .message(sb.toString())
                .build();

        mockMvc.perform(post(RoomController.BASE_URL + "1/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chatMessageCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.errors.message[0]", equalTo(ChatMessageCommand.MESSAGE_LENGTH_ERROR)))
                .andExpect(jsonPath("$.errors.message", hasSize(1)));
    }
}
