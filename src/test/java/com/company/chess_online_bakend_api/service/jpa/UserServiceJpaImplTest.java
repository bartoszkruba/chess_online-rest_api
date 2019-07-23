package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.converter.user.UserToUserCommand;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.exception.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceJpaImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserToUserCommand userToUserCommand;

    @InjectMocks
    UserServiceJpaImpl userService;

    private static final Long USER1_ID = 1L;
    private static final String USERNAME1 = "john69";
    private static final Long USER2_ID = 2L;
    private static final String USERNAME2 = "john70";

    private User returnUser1;
    private User returnUser2;

    private UserCommand returnUserCommand1;
    private UserCommand returnUserCommand2;

    @BeforeEach
    void setUp() {

        returnUser1 = User.builder()
                .id(USER1_ID)
                .username(USERNAME1).build();

        returnUserCommand1 = UserCommand.builder()
                .id(USER1_ID)
                .username(USERNAME1).build();

        returnUser2 = User.builder()
                .id(USER2_ID)
                .username(USERNAME2).build();

        returnUserCommand2 = UserCommand.builder()
                .id(USER2_ID)
                .username(USERNAME2).build();
    }

    @Test
    void findById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(returnUser1));
        when(userToUserCommand.convert(returnUser1)).thenReturn(returnUserCommand1);

        UserCommand user = userService.findById(USER1_ID);

        assertNotNull(user);
        assertEquals(USER1_ID, user.getId());
        assertEquals(USERNAME1, user.getUsername());

        verify(userRepository, times(1)).findById(USER1_ID);
        verifyNoMoreInteractions(userRepository);

        verify(userToUserCommand, times(1)).convert(returnUser1);
        verifyNoMoreInteractions(userToUserCommand);
    }

    @Test
    void findByIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findById(USER1_ID));

        verify(userRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
        verifyZeroInteractions(userToUserCommand);
    }

    @Test
    @Disabled
    void save() {
//        User userToSave = User.builder().username(USERNAME1).build();
//
//        when(userRepository.save(any())).thenReturn(returnUser1);
//
//        User user = userService.save(userToSave);
//
//        assertNotNull(user);
//        assertEquals(USERNAME1, user.getUsername());
//
//        verify(userRepository, times(1)).save(userToSave);
    }

    @Test
    @Disabled
    void findAll() {
//        List<User> returnUserList = new ArrayList<>();
//        returnUserList.add(returnUser1);
//        returnUserList.add(returnUser2);
//
//        when(userRepository.findAll()).thenReturn(returnUserList);
//
//        Set<User> users = userService.findAll();
//
//        assertEquals(2, users.size());
//
//        assertTrue(users.contains(returnUser1));
//        assertTrue(users.contains(returnUser2));
//
//        verify(userRepository, times(1)).findAll();
    }

    @Test
    @Disabled
    void delete() {
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(returnUser1));
//
//        userService.delete(returnUser1);
//
//        verify(userRepository, times(1)).delete(returnUser1);
    }

    @Test
    @Disabled
    void deleteUserNotFound() {
//        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(RuntimeException.class, () -> userService.delete(returnUser1));
    }

    @Test
    @Disabled
    void deleteIdIsNull() {
//        Assertions.assertThrows(RuntimeException.class, () -> userService.delete(null));
    }

    @Test
    @Disabled
    void deleteById() {
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(returnUser1));
//
//        userService.deleteById(USER1_ID);
//
//        verify(userRepository, times(1)).deleteById(USER1_ID);
    }

    @Test
    @Disabled
    void deleteByIdUserNotFound() {
//        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(RuntimeException.class, () -> userService.deleteById(USER1_ID));
    }

    @Test
    @Disabled
    void deleteByIdIsNull() {
//        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
//        Assertions.assertThrows(RuntimeException.class, () -> userService.deleteById(USER1_ID));
    }

    @Test
    void findByUsername() {
        when(userRepository.findByUsernameLike(anyString())).thenReturn(Optional.of(returnUser1));
        when(userToUserCommand.convert(returnUser1)).thenReturn(returnUserCommand1);

        UserCommand user = userService.findByUsername(USERNAME1);

        assertEquals(USERNAME1, user.getUsername());
        assertEquals(USER1_ID, user.getId());

        verify(userRepository, times(1)).findByUsernameLike(USERNAME1);
        verifyNoMoreInteractions(userRepository);

        verify(userToUserCommand, times(1)).convert(returnUser1);
        verifyNoMoreInteractions(userToUserCommand);
    }

    @Test
    void findByUsernameNotFound() {
        when(userRepository.findByUsernameLike(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findByUsername(USERNAME1));

        verify(userRepository, times(1)).findByUsernameLike(anyString());
        verifyNoMoreInteractions(userRepository);

        verifyZeroInteractions(userToUserCommand);
    }

    @Test
    void findByUsernameIsNull() {
        UserCommand user = userService.findByUsername(null);

        assertNull(user);

        verifyZeroInteractions(userRepository);
        verifyZeroInteractions(userToUserCommand);
    }
}