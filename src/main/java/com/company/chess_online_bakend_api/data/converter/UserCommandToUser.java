package com.company.chess_online_bakend_api.data.converter;

import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UserCommandToUser implements Converter<UserCommand, User> {

    @Nullable
    @Override
    public User convert(UserCommand userCommand) {

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
