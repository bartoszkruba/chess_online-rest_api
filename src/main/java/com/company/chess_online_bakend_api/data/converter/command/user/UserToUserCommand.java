/*
 * 7/27/19 3:27 PM. Updated by Bartosz Kruba.
 */

/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.command.user;

import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserToUserCommand implements Converter<User, UserCommand> {

    @Nullable
    @Override
    public UserCommand convert(User user) {

        log.debug("Converting User to UserCommand");

        if (user == null) {
            return null;
        }

        final var userCommand = UserCommand.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
//                .password(user.getPassword())
                .id(user.getId()).build();

        return userCommand;
    }
}
