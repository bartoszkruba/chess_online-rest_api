/*
 * 7/27/19 5:29 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.notification.user;

import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.notification.UserNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserToUserNotification implements Converter<User, UserNotification> {

    @Override
    @Nullable
    public UserNotification convert(User user) {
        log.debug("Converting User to UserNotification");

        if (user == null) {
            return null;
        }

        return UserNotification.builder().id(user.getId()).username(user.getUsername()).build();
    }
}
