package com.company.chess_online_bakend_api.controller;

import com.company.chess_online_bakend_api.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTest extends AbstractRestControllerTest {

    private final Long ID = 1L;
    private final String USERNAME = "john69";
    private final String PASSWORD = "password";
    private final String FIRST_NAME = "John";
    private final String LAST_NAME = "Doe";
    private final String EMAIL = "johm.doe@email.com";

    @Mock
    Principal principal;

    @Mock
    AuthenticationService authenticationService;

    @InjectMocks
    AuthenticationController authenticationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {


        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(ExceptionAdviceController.class)
                .build();
    }

    @Test
    void getRoles() throws Exception {
        List<String> roles = Arrays.asList("ROLE_ADMIN", "ROLE_USER");

        when(principal.getName()).thenReturn(USERNAME);
        when(authenticationService.getRolesForUser(USERNAME)).thenReturn(roles);

        mockMvc.perform(get(AuthenticationController.BASE_URL + "role")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}