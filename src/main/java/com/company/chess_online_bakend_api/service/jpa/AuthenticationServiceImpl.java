package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.converter.UserCommandToUser;
import com.company.chess_online_bakend_api.data.converter.UserToUserCommand;
import com.company.chess_online_bakend_api.data.model.Role;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserCommandToUser userCommandToUser;
    private final UserToUserCommand userToUserCommand;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, UserCommandToUser userCommandToUser, UserToUserCommand
            userToUserCommand, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userCommandToUser = userCommandToUser;
        this.userToUserCommand = userToUserCommand;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Collection<String> getRolesForUser(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        log.debug("Getting roles for user:" + username);

        // TODO: 2019-06-29 create custom exception for no user found
        // TODO: 2019-06-29 create advice for handling exception
        if (userOptional.isEmpty()) {
            log.debug("User not found: " + username);
            throw new RuntimeException();
        }

        return userOptional.get()
                .getRoles().stream()
                .map(Role::getAuthority).collect(Collectors.toSet());
    }

    @Override
    public UserCommand registerNewUser(UserCommand userCommand) {
        log.debug("Registering new user: " + userCommand);

        User user = userCommandToUser.convert(userCommand);

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            // TODO: 2019-06-30 Crate custom exception and advice
            log.debug("Registering new user failed: username already exists");
            throw new RuntimeException("Username already exists");
        }

        user.setId(null);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userToUserCommand.convert(userRepository.save(user));
    }
}
