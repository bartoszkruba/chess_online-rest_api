package com.company.chess_online_bakend_api.config;


import com.company.chess_online_bakend_api.controller.AuthenticationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class LoginTest {

    // TODO: 2019-06-23 Try to create some mock users for testing

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void adminCanLog() throws Exception {
        mockMvc.perform(formLogin().loginProcessingUrl(AuthenticationController.BASE_URL + "login")
                .user("ken123")
                .password("devo"))
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("ken123"));

        // TODO: 2019-06-23 figure out why it's not possible to assert roles

        mockMvc.perform(logout().logoutUrl("/auth/logout"))
                .andExpect(status().isOk());
    }

    @Test
    void userCanLog() throws Exception {
        mockMvc.perform(formLogin().loginProcessingUrl(AuthenticationController.BASE_URL + "login")
                .user("carl69")
                .password("tyler1"))
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("carl69"));
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
