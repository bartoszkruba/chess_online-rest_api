package com.company.chess_online_bakend_api.controller;

import com.company.chess_online_bakend_api.bootstrap.UserBootstrap;
import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.repository.RoleRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.data.validation.constraint.ValidPasswordConstraint;
import com.company.chess_online_bakend_api.data.validation.constraint.ValidUsernameConstraint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        userBootstrap.onApplicationEvent(null);

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    void rolesNotLoggedIn() throws Exception {
        mockMvc.perform(get(AuthenticationController.BASE_URL + "role"))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "carl69", password = "tyler1", authorities = "ROLE_USER")
    void rolesLoggedInAsUser() throws Exception {
        mockMvc.perform(get(AuthenticationController.BASE_URL + "role"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ken123", password = "devo", authorities = "ROLE_ADMIN")
    void rolesLoggedInAsAdmin() throws Exception {
        mockMvc.perform(get(AuthenticationController.BASE_URL + "role"))
                .andExpect(status().isOk());
    }


    @Test
    void registerNewUserUsernameAlreadyExists() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username("ken123")
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
                .andExpect(jsonPath("$.errors[0]", equalTo("Username already exists")))
                .andExpect(jsonPath("$.errors", hasSize(1)));

    }

    @Test
    void registerNewUserUsernameNull() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username(null)
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
                .andExpect(jsonPath("$.errors[0]", equalTo(UserCommand.USERNAME_NOT_EMPTY_MESSAGE)))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void registerNewUserUsernameTooShort() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username("u")
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
                .andExpect(jsonPath("$.errors[0]", equalTo(UserCommand.USERNAME_SIZE_MESSAGE)))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void registerNewUserUsernameTooLong() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username("thisIsAVeryLongUsernamesddssdsdsdsdsddssdsdsdsddsdssdsdsd")
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
                .andExpect(jsonPath("$.errors[0]", equalTo(UserCommand.USERNAME_SIZE_MESSAGE)))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void registerNewUserInvalidUsername() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username("This is invalid username")
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
                .andExpect(jsonPath("$.errors[0]", equalTo(ValidUsernameConstraint.ERROR_MESSAGE)))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void registerNewUserEmptyUsername() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username("")
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
                .andExpect(jsonPath("$.errors", containsInAnyOrder(UserCommand.USERNAME_NOT_EMPTY_MESSAGE,
                        UserCommand.USERNAME_SIZE_MESSAGE)))
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @Test
    void registerNewUserEmptyPassword() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username(USERNAME)
                .password("")
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();

        mockMvc.perform(post(AuthenticationController.BASE_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.errors",
                        containsInAnyOrder(UserCommand.PASSWORD_NOT_SIZE_MESSAGE,
                                UserCommand.PASSWORD_NOT_EMPTY_MESSAGE)))
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @Test
    void registerNewUserPasswordTooLong() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username(USERNAME)
                .password("dsfsfadofsdkofdsaokfdsaokosdfkopfdsopksdfkfdkspdfakpksdafpkfdspkodsfpksdfkpdsfkpsdafpopkdas")
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();

        mockMvc.perform(post(AuthenticationController.BASE_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.errors[0]", equalTo(UserCommand.PASSWORD_NOT_SIZE_MESSAGE)))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void registerNewUserPasswordNull() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username(USERNAME)
                .password(null)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();

        mockMvc.perform(post(AuthenticationController.BASE_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.errors[0]", equalTo(UserCommand.PASSWORD_NOT_EMPTY_MESSAGE)))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void registerInvalidPassword() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username(USERNAME)
                .password("This is invali'ä'äåsdaö")
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();

        mockMvc.perform(post(AuthenticationController.BASE_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.errors[0]", equalTo(ValidPasswordConstraint.ERROR_MESSAGE)))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void registerInvalidEmail() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .email("invalid email")
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();

        mockMvc.perform(post(AuthenticationController.BASE_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.errors[0]", equalTo(UserCommand.EMAIL_NOT_VALID_MESSAGE)))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void registerEmailNull() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .email(null)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();

        mockMvc.perform(post(AuthenticationController.BASE_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.errors[0]", equalTo(UserCommand.EMAIL_NOT_EMPTY_MESSAGE)))
                .andExpect(jsonPath("$.errors", hasSize(1)));
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
                .andExpect(jsonPath("$.username", equalTo(USERNAME)))
                .andExpect(jsonPath("$.firstName", equalTo(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", equalTo(LAST_NAME)))
                .andExpect(jsonPath("$.email", equalTo(EMAIL)));
    }
}
