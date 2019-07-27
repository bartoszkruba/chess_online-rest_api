/*
 * 7/27/19 5:33 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.notification.user;

import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.notification.UserNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserToUserNotificationTest {

    @InjectMocks
    UserToUserNotification userToUserNotification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void convertNull() {
        assertNull(userToUserNotification.convert(null));
    }

    @Test
    void convertEmptyObject() {
        var userNotification = userToUserNotification.convert(User.builder().build());

        assertNull(userNotification.getId());
        assertNull(userNotification.getUsername());
    }

    @Test
    void convert() {
        String username = "username";
        Long userId = 1L;

        var userNotfication = UserNotification.builder().username(username).id(userId).build();

        assertEquals(username, userNotfication.getUsername());
        assertEquals(userId, userNotfication.getId());
    }
}