package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.converter.UserCommandToUser;
import com.company.chess_online_bakend_api.data.converter.UserToUserCommand;
import com.company.chess_online_bakend_api.data.model.Role;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.exception.UserNotFoundException;
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

        if (userOptional.isEmpty()) {
            log.debug("User not found: " + username);
            throw new UserNotFoundException("User not found: " + username);
        }

        return userOptional.get()
                .getRoles().stream()
                .map(Role::getAuthority).collect(Collectors.toSet());
    }

    @Override
    public UserCommand registerNewUser(UserCommand userCommand) {
        log.debug("Registering new user: " + userCommand);

        User user = userCommandToUser.convert(userCommand);

        user.setId(null);
        if (user.getPassword() == null) {
            log.error("Password is null");
            throw new RuntimeException("Password is null");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userToUserCommand.convert(userRepository.save(user));
    }
}
