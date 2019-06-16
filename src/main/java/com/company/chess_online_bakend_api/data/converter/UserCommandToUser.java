package com.company.chess_online_bakend_api.data.converter;

import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.model.User;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UserCommandToUser implements Converter<UserCommand, User> {

    @Synchronized
    @Nullable
    @Override
    public User convert(UserCommand userCommand) {
        User user = User.builder()
                .firstName(userCommand.getFirstName())
                .lastName(userCommand.getLastName())
                .email(userCommand.getEmail())
                .username(userCommand.getUsername())
                .password(userCommand.getPassword())
                .profileImage(userCommand.getProfileImage())
                .id(userCommand.getId())
                .role(userCommand.getRole()).build();

        return user;
    }
}
