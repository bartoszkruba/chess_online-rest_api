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
public class UserCommandToUser implements Converter<UserCommand, User> {

    @Nullable
    @Override
    public User convert(UserCommand userCommand) {

        log.debug("Converting UserCommand to User");

        if (userCommand == null) {
            return null;
        }

        return User.builder()
                .firstName(userCommand.getFirstName())
                .lastName(userCommand.getLastName())
                .email(userCommand.getEmail())
                .username(userCommand.getUsername())
                .password(userCommand.getPassword())
                .id(userCommand.getId()).build();
    }
}
