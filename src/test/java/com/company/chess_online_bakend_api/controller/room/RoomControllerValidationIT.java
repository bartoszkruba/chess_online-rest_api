package com.company.chess_online_bakend_api.controller.room;

import com.company.chess_online_bakend_api.controller.AbstractRestControllerTest;
import com.company.chess_online_bakend_api.controller.RoomController;
import com.company.chess_online_bakend_api.data.command.RoomCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomControllerValidationIT extends AbstractRestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    void createNewGameNameNull() throws Exception {
        RoomCommand roomCommand = RoomCommand.builder().build();

        mockMvc.perform(post(RoomController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(roomCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.errors.name[0]", equalTo(RoomCommand.NAME_NOT_EMPTY_MESSAGE)))
                .andExpect(jsonPath("$.errors.name", hasSize(1)));
    }

    @Test
    void createNewGameNameTooShort() throws Exception {
        RoomCommand roomCommand = RoomCommand.builder().name("a").build();

        mockMvc.perform(post(RoomController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(roomCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.errors.name[0]", equalTo(RoomCommand.NAME_SIZE_MESSAGE)))
                .andExpect(jsonPath("$.errors.name", hasSize(1)));
    }

    @Test
    void createNewGameNameTooLong() throws Exception {
        RoomCommand roomCommand = RoomCommand.builder().name("adsffdsfsdfsdfsdfdsfsdfdsfsdfdsfdsfsdfsdfsdfsdsfdsfdsfdsf" +
                "dsdfsdffsdfsdsfdfsdsfdsfdsfdsfdsfdfsdfsdfsdfsdfsdfdssfdfsdfsdfsdsdfsdffdsdfs").build();

        mockMvc.perform(post(RoomController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(roomCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.errors.name[0]", equalTo(RoomCommand.NAME_SIZE_MESSAGE)))
                .andExpect(jsonPath("$.errors.name", hasSize(1)));
    }
}
