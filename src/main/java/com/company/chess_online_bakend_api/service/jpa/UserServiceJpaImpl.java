package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.converter.user.UserToUserCommand;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.exception.UserNotFoundException;
import com.company.chess_online_bakend_api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class UserServiceJpaImpl implements UserService {

    private final UserRepository userRepository;
    private final UserToUserCommand userToUserCommand;

    @Autowired
    public UserServiceJpaImpl(UserRepository userRepository, UserToUserCommand userToUserCommand) {
        this.userRepository = userRepository;
        this.userToUserCommand = userToUserCommand;
    }

    @Override
    public UserCommand findById(Long id) {
        if (id == null) {
            log.error("Id is null");
            throw new RuntimeException("Id is null");
        }

        log.debug("Getting user with id: " + id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " does not exist"));

        return userToUserCommand.convert(user);
    }

    @Override
    public UserCommand save(UserCommand object) {
        return null;
    }

    @Override
    public Set<UserCommand> findAll() {
        return null;
    }

    @Override
    public void delete(UserCommand object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public UserCommand findByUsername(String username) {
        if (username == null) {
            log.error("Username is null");
            return null;
        }

        log.debug("Getting user with username: " + username);
        User user = userRepository.findByUsernameLike(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " does not exist"));

        return userToUserCommand.convert(user);
    }

}
