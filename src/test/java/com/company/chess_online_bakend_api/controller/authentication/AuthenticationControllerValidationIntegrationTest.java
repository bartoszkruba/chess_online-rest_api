/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.controller.authentication;

import com.company.chess_online_bakend_api.controller.AbstractRestControllerTest;
import com.company.chess_online_bakend_api.controller.AuthenticationController;
import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.validation.constraint.ValidNameConstraint;
import com.company.chess_online_bakend_api.data.validation.constraint.ValidPasswordConstraint;
import com.company.chess_online_bakend_api.data.validation.constraint.ValidUsernameConstraint;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerValidationIntegrationTest extends AbstractRestControllerTest {

    private MockMvc mockMvc;

    private final Long ID = 1L;
    private final String USERNAME = "john69";
    private final String PASSWORD = "password";
    private final String FIRST_NAME = "John";
    private final String LAST_NAME = "Doe";
    private final String EMAIL = "johm.doe@email.com";

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
                .andExpect(jsonPath("$.errors.username[0]", equalTo(UserCommand.USERNAME_NOT_EMPTY_MESSAGE)))
                .andExpect(jsonPath("$.errors.username", hasSize(1)));
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
                .andExpect(jsonPath("$.errors.username[0]", equalTo(UserCommand.USERNAME_SIZE_MESSAGE)))
                .andExpect(jsonPath("$.errors.username", hasSize(1)));
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
                .andExpect(jsonPath("$.errors.username[0]", equalTo(UserCommand.USERNAME_SIZE_MESSAGE)))
                .andExpect(jsonPath("$.errors.username", hasSize(1)));
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
                .andExpect(jsonPath("$.errors.username[0]", equalTo(ValidUsernameConstraint.ERROR_MESSAGE)))
                .andExpect(jsonPath("$.errors.username", hasSize(1)));
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
                .andExpect(jsonPath("$.errors.username", containsInAnyOrder(UserCommand.USERNAME_NOT_EMPTY_MESSAGE,
                        UserCommand.USERNAME_SIZE_MESSAGE)))
                .andExpect(jsonPath("$.errors.username", hasSize(2)));
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
                .andExpect(jsonPath("$.errors.password",
                        containsInAnyOrder(UserCommand.PASSWORD_NOT_SIZE_MESSAGE,
                                UserCommand.PASSWORD_NOT_EMPTY_MESSAGE)))
                .andExpect(jsonPath("$.errors.password", hasSize(2)));
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
                .andExpect(jsonPath("$.errors.password[0]", equalTo(UserCommand.PASSWORD_NOT_SIZE_MESSAGE)))
                .andExpect(jsonPath("$.errors.password", hasSize(1)));
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
                .andExpect(jsonPath("$.errors.password[0]", equalTo(UserCommand.PASSWORD_NOT_EMPTY_MESSAGE)))
                .andExpect(jsonPath("$.errors.password", hasSize(1)));
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
                .andExpect(jsonPath("$.errors.password[0]", equalTo(ValidPasswordConstraint.ERROR_MESSAGE)))
                .andExpect(jsonPath("$.errors.password", hasSize(1)));
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
                .andExpect(jsonPath("$.errors.email[0]", equalTo(UserCommand.EMAIL_NOT_VALID_MESSAGE)))
                .andExpect(jsonPath("$.errors.email", hasSize(1)));
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
                .andExpect(jsonPath("$.errors.email[0]", equalTo(UserCommand.EMAIL_NOT_EMPTY_MESSAGE)))
                .andExpect(jsonPath("$.errors.email", hasSize(1)));
    }

    @Test
    void registerInvalidName() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .email(null)
                .firstName("%%%%%%%%")
                .lastName("¤¤¤¤¤¤¤¤")
                .build();

        mockMvc.perform(post(AuthenticationController.BASE_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.errors.firstName[0]", equalTo(ValidNameConstraint.ERROR_MESSAGE)))
                .andExpect(jsonPath("$.errors.firstName", hasSize(1)))
                .andExpect(jsonPath("$.errors.lastName[0]", equalTo(ValidNameConstraint.ERROR_MESSAGE)))
                .andExpect(jsonPath("$.errors.lastName", hasSize(1)));
    }
}
