package com.company.chess_online_bakend_api.controller.room;

import com.company.chess_online_bakend_api.bootstrap.dev.RoomBootstrap;
import com.company.chess_online_bakend_api.bootstrap.dev.UserBootstrap;
import com.company.chess_online_bakend_api.controller.AbstractRestControllerTest;
import com.company.chess_online_bakend_api.controller.RoomController;
import com.company.chess_online_bakend_api.data.command.RoomCommand;
import com.company.chess_online_bakend_api.data.repository.RoleRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.data.validation.constraint.UniqueRoomNameConstraint;
import org.hamcrest.Matchers;
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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class RoomControllerIT extends AbstractRestControllerTest {

    private final Long ROOMCOMMAND1_ID = 3L;

    private final String ROOMCOMMAND1_NAME = "Room Name 1";

    private final RoomCommand ROOMCOMMAND1 = RoomCommand.builder().id(ROOMCOMMAND1_ID)
            .name(ROOMCOMMAND1_NAME).build();

    private MockMvc mockMvc;

    @Autowired
    private UserBootstrap userBootstrap;

    @Autowired
    private RoomBootstrap roomBootstrap;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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
        userRepository.deleteAll();
        roleRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    void createNewRoomNotLoggedIn() throws Exception {
        mockMvc.perform(post(RoomController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(ROOMCOMMAND1)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = UserBootstrap.ROLE_USER)
    void createNewRoomLoggedInAsUser() throws Exception {
        mockMvc.perform(post(RoomController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(ROOMCOMMAND1)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = UserBootstrap.ROLE_USER)
    void createNewRoomNameAlreadyExists() throws Exception {
        mockMvc.perform(post(RoomController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(RoomCommand.builder().name("Alpha").build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", Matchers.equalTo(400)))
                .andExpect(jsonPath("$.errors[0]", Matchers.equalTo(UniqueRoomNameConstraint.ERROR_MESSAGE)))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    @WithMockUser(authorities = UserBootstrap.ROLE_ADMIN)
    void createNewRoom() throws Exception {
        mockMvc.perform(post(RoomController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(ROOMCOMMAND1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(ROOMCOMMAND1_NAME)))
                .andExpect(jsonPath("$.id", not(ROOMCOMMAND1_ID)));
    }

    @Test
    void deleteRoomNotLoggedIn() throws Exception {
        mockMvc.perform(delete(RoomController.BASE_URL + ROOMCOMMAND1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = UserBootstrap.ROLE_USER)
    void deleteRoomLoggedInAsUser() throws Exception {
        mockMvc.perform(delete(RoomController.BASE_URL + ROOMCOMMAND1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = UserBootstrap.ROLE_ADMIN)
    void deleteRoomLoggedInAsAdmin() throws Exception {
        mockMvc.perform(delete(RoomController.BASE_URL + roomRepository.findByNameLike("Alpha").get().getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
