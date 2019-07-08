package com.company.chess_online_bakend_api.data.converter;

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

        final UserCommand userCommand = UserCommand.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
//                .password(user.getPassword())
                .id(user.getId()).build();

        return userCommand;
    }
}
