package com.company.chess_online_bakend_api.controller;

import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.exception.UserNotFoundException;
import com.company.chess_online_bakend_api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    @Mock
    private UserService userService;


    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(ExceptionAdviceController.class)
                .build();

    }


    @Test
    void getUserByIdNotFound() throws Exception {
        Long userId = 1L;

        when(userService.findById(userId)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(get(UserController.BASE_URL + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)));

        verify(userService, times(1)).findById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getUserById() throws Exception {
        Long userId = 1L;
        String username = "username";

        UserCommand userCommand = UserCommand.builder().id(userId).username(username).build();

        when(userService.findById(userId)).thenReturn(userCommand);

        mockMvc.perform(get(UserController.BASE_URL + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(userId.intValue())))
                .andExpect(jsonPath("$.username", equalTo(username)));

        verify(userService, times(1)).findById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getUserByUsernameNotFound() throws Exception {
        String username = "username";

        when(userService.findByUsername(username)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(get(UserController.BASE_URL + "username/" + username)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)));

        verify(userService, times(1)).findByUsername(username);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getUserByUsername() throws Exception {
        Long userId = 1L;
        String username = "username";

        UserCommand userCommand = UserCommand.builder().id(userId).username(username).build();

        when(userService.findByUsername(username)).thenReturn(userCommand);

        mockMvc.perform(get(UserController.BASE_URL + "username/" + username)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(userId.intValue())))
                .andExpect(jsonPath("$.username", equalTo(username)));

        verify(userService, times(1)).findByUsername(username);
        verifyNoMoreInteractions(userService);
    }
}