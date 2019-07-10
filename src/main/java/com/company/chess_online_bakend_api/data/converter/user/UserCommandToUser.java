package com.company.chess_online_bakend_api.data.converter.user;

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
