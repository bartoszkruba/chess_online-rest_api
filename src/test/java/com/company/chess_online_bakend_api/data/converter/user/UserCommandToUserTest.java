/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.user;

import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.converter.command.user.UserCommandToUser;
import com.company.chess_online_bakend_api.data.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserCommandToUserTest {

    private static final Long USER_ID = 1L;
    private static final String USERNAME = "john69";
    private static final String PASSWORD = "password1234";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "doe";
    private static final String EMAIL = "john.doe@gmail.com";

    private UserCommandToUser commandToUser;

    @BeforeEach
    void setUp() {
        commandToUser = new UserCommandToUser();
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(commandToUser.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        User convertedUser = commandToUser.convert(UserCommand.builder().build());

        assertNotNull(convertedUser);
        assertNull(convertedUser.getId());
        assertNull(convertedUser.getUsername());
        assertNull(convertedUser.getEmail());
        assertNull(convertedUser.getFirstName());
        assertNull(convertedUser.getLastName());
        assertNull(convertedUser.getPassword());
        assertNull(convertedUser.getProfileImage());
        assertNull(convertedUser.getCreated());
        assertNull(convertedUser.getUpdated());
    }

    @Test
    public void testConvert() throws Exception {
        UserCommand userToConvert = UserCommand.builder()
                .id(USER_ID)
                .username(USERNAME)
                .password(PASSWORD)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL).build();

        User convertedUser = commandToUser.convert(userToConvert);

        assertNotNull(convertedUser);
        assertEquals(USER_ID, convertedUser.getId());
        assertEquals(USERNAME, convertedUser.getUsername());
        assertEquals(PASSWORD, convertedUser.getPassword());
        assertEquals(FIRST_NAME, convertedUser.getFirstName());
        assertEquals(LAST_NAME, convertedUser.getLastName());
        assertEquals(EMAIL, convertedUser.getEmail());

        assertNull(convertedUser.getCreated());
        assertNull(convertedUser.getUpdated());
        assertNull(convertedUser.getProfileImage());

        assertEquals(0, convertedUser.getRoles().size());
    }
}