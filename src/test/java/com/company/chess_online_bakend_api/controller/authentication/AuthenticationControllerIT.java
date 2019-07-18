package com.company.chess_online_bakend_api.controller.authentication;

import com.company.chess_online_bakend_api.bootstrap.dev.UserBootstrap;
import com.company.chess_online_bakend_api.controller.AbstractRestControllerTest;
import com.company.chess_online_bakend_api.controller.AuthenticationController;
import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.repository.RoleRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.data.validation.constraint.UniqueUsernameConstraint;
import com.company.chess_online_bakend_api.util.StringUtils;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class AuthenticationControllerIT extends AbstractRestControllerTest {
    private MockMvc mockMvc;

    private final Long ID = 1L;
    private final String USERNAME = "john69";
    private final String PASSWORD = "password";
    private final String FIRST_NAME = "John";
    private final String LAST_NAME = "Doe";
    private final String EMAIL = "johm.doe@email.com";


    @Autowired
    private UserBootstrap userBootstrap;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setup() {
        userBootstrap.onApplicationEvent(null);

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void rolesNotLoggedIn() throws Exception {
        mockMvc.perform(get(AuthenticationController.BASE_URL + "role"))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(authorities = UserBootstrap.ROLE_USER)
    void rolesLoggedInAsUser() throws Exception {
        mockMvc.perform(get(AuthenticationController.BASE_URL + "role"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = UserBootstrap.ROLE_ADMIN)
    void rolesLoggedInAsAdmin() throws Exception {
        mockMvc.perform(get(AuthenticationController.BASE_URL + "role"))
                .andExpect(status().isOk());
    }


    @Test
    void registerNewUserUsernameAlreadyExists() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username(UserBootstrap.USER_USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();

        mockMvc.perform(post(AuthenticationController.BASE_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.errors[0]", equalTo(UniqueUsernameConstraint.ERROR_MESSAGE)))
                .andExpect(jsonPath("$.errors", hasSize(1)));

    }

    @Test
    void registerNewUserCapitalizedUsername() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username(USERNAME.toUpperCase())
                .password(PASSWORD)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL).build();

        mockMvc.perform(post(AuthenticationController.BASE_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", equalTo(StringUtils.capitalizeFirstLetter(USERNAME))))
                .andExpect(jsonPath("$.firstName", equalTo(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", equalTo(LAST_NAME)))
                .andExpect(jsonPath("$.email", equalTo(EMAIL)));
    }

    @Test
    void registerNewUser() throws Exception {

        UserCommand userCommand = UserCommand.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL).build();

        mockMvc.perform(post(AuthenticationController.BASE_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", equalTo(StringUtils.capitalizeFirstLetter(USERNAME))))
                .andExpect(jsonPath("$.firstName", equalTo(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", equalTo(LAST_NAME)))
                .andExpect(jsonPath("$.email", equalTo(EMAIL)));
    }
}
