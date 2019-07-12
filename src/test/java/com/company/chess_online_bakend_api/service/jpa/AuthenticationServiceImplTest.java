package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.converter.user.UserCommandToUser;
import com.company.chess_online_bakend_api.data.converter.user.UserToUserCommand;
import com.company.chess_online_bakend_api.data.model.Role;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.RoleRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    AuthenticationServiceImpl authenticationService;

    private final Long ID = 1L;
    private final String PASSWORD = "password";
    private final String FIRST_NAME = "John";
    private final String LAST_NAME = "Doe";
    private final String EMAIL = "john.doe@email.com";

    private final String USERNAME = "john69";
    private final String ROLE_ADMIN = "ROLE_ADMIN";
    private final String ROLE_USER = "ROLE_USER";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        authenticationService = new AuthenticationServiceImpl(userRepository, roleRepository, new UserCommandToUser(),
                new UserToUserCommand(), bCryptPasswordEncoder);
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

        when(userRepository.findByUsernameLike(USERNAME)).thenReturn(Optional.of(user));

        Collection<String> returnedRoles = authenticationService.getRolesForUser(USERNAME);

        assertEquals(2, returnedRoles.size());
        assertTrue(returnedRoles.contains(ROLE_ADMIN));
        assertTrue(returnedRoles.contains(ROLE_USER));

        verify(userRepository, times(1)).findByUsernameLike(USERNAME);
    }

    @Test
    void getRolesUsernameNotFound() {
        when(userRepository.findByUsernameLike(USERNAME)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> authenticationService.getRolesForUser(USERNAME));
    }

    @Test
    void registerNewUser() {
        UserCommand userCommand = UserCommand.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL).build();

        User savedUser = User.builder()
                .id(ID)
                .username(USERNAME)
                .password(bCryptPasswordEncoder.encode(PASSWORD))
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL).build();

        Role role = Role.builder().description(ROLE_USER).build();

        when(roleRepository.findByDescription(ROLE_USER)).thenReturn(role);
        when(userRepository.save(any())).thenReturn(savedUser);

        UserCommand returnedUser = authenticationService.registerNewUser(userCommand);

        assertEquals(ID, returnedUser.getId());
        assertEquals(USERNAME, returnedUser.getUsername());
        assertNull(returnedUser.getPassword());
        assertEquals(FIRST_NAME, returnedUser.getFirstName());
        assertEquals(LAST_NAME, returnedUser.getLastName());
        assertEquals(EMAIL, returnedUser.getEmail());

        verify(userRepository, times(1)).save(any());
        verify(roleRepository, times(1)).findByDescription(ROLE_USER);
    }
}