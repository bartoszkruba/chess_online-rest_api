package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.model.Role;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AuthenticationServiceImpl authenticationService;

    private final String USERNAME = "john69";
    private final String ROLE_ADMIN = "ROLE_ADMIN";
    private final String ROLE_USER = "ROLE_USER";


    @BeforeEach
    void setUp() {
    }

    @Test
    void getRolesForUser() {
        Set<Role> roles = new HashSet<>();

        roles.add(Role.builder().description(ROLE_ADMIN)
                .id(1L).build());
        roles.add(Role.builder().description(ROLE_USER)
                .id(2L).build());

        User user = User.builder()
                .id(3L)
                .username(USERNAME)
                .roles(roles).build();

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        Collection<String> returnedRoles = authenticationService.getRolesForUser(USERNAME);

        assertEquals(2, returnedRoles.size());
        assertTrue(returnedRoles.contains(ROLE_ADMIN));
        assertTrue(returnedRoles.contains(ROLE_USER));

        verify(userRepository, times(1)).findByUsername(USERNAME);
    }

    @Test
    void getRolesUsernameNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> authenticationService.getRolesForUser(USERNAME));
    }
}