package com.company.chess_online_bakend_api.data.converter;

import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserToUserCommandTest {

    private final Long USER_ID = 1L;
    private static final String USERNAME = "john69";
    private static final String PASSWORD = "password1234";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String EMAIL = "john.doe@gmail.com";

    private UserToUserCommand userToUserCommand;

    @BeforeEach
    void setUp() {
        userToUserCommand = new UserToUserCommand();
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(userToUserCommand.convert(null));
    }

    @Test
    public void testConvertEmptyObject() throws Exception {
        UserCommand convertedUser = userToUserCommand.convert(User.builder().build());

        assertNotNull(convertedUser);

        assertNull(convertedUser.getId());
        assertNull(convertedUser.getUsername());
//        assertNull(convertedUser.getPassword());
        assertNull(convertedUser.getFirstName());
        assertNull(convertedUser.getLastName());
        assertNull(convertedUser.getEmail());
    }

    @Test
    public void testConvertUser() throws Exception {
        User userToConvert = User.builder()
                .id(USER_ID)
                .username(USERNAME)
//                .password(PASSWORD)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL).build();

        UserCommand convertedUser = userToUserCommand.convert(userToConvert);

        assertNotNull(convertedUser);

        assertNotNull(convertedUser);
        assertEquals(USER_ID, convertedUser.getId());
        assertEquals(USERNAME, convertedUser.getUsername());
//        assertEquals(PASSWORD, convertedUser.getPassword());
        assertEquals(FIRST_NAME, convertedUser.getFirstName());
        assertEquals(LAST_NAME, convertedUser.getLastName());
        assertEquals(EMAIL, convertedUser.getEmail());
    }
}