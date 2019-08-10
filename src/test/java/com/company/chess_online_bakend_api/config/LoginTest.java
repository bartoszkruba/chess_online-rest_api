/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.config;


import com.company.chess_online_bakend_api.bootstrap.dev.UserBootstrap;
import com.company.chess_online_bakend_api.controller.AuthenticationController;
import com.company.chess_online_bakend_api.data.repository.RoleRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class LoginTest {

    private MockMvc mockMvc;

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
    public void adminCanLog() throws Exception {
        mockMvc.perform(formLogin().loginProcessingUrl(AuthenticationController.BASE_URL + "login")
                .user(UserBootstrap.ADMIN_USERNAME)
                .password(UserBootstrap.ADMIN_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername(UserBootstrap.ADMIN_USERNAME));

        mockMvc.perform(logout().logoutUrl("/auth/logout"))
                .andExpect(status().isOk());
    }

    @Test
    void userCanLog() throws Exception {
        mockMvc.perform(formLogin().loginProcessingUrl(AuthenticationController.BASE_URL + "login")
                .user(UserBootstrap.USER_USERNAME)
                .password(UserBootstrap.USER_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername(UserBootstrap.USER_USERNAME));
//                .andExpect(authenticated().withRoles("USER"));

        mockMvc.perform(logout().logoutUrl("/auth/logout"))
                .andExpect(status().isOk());
    }

    @Test
    void userDoesntExists() throws Exception {
        mockMvc.perform(formLogin().loginProcessingUrl(AuthenticationController.BASE_URL + "login")
                .user("do not exists")
                .password("password"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void passwordDoesntMatch() throws Exception {
        mockMvc.perform(formLogin().loginProcessingUrl(AuthenticationController.BASE_URL + "login")
                .user("carl69")
                .password("password"))
                .andExpect(status().is4xxClientError());
    }
}
