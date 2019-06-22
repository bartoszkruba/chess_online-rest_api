package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserServiceJpaImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceJpaImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long id) {

        if (id == null) {
            log.error("Id is null");
            throw new RuntimeException("Id is null");
        }

        log.debug("Getting user with id: " + id);

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            log.debug("User with following id not found: " + id);
            // TODO: 2019-06-22 create custom exception and handle error thourgh advice
            throw new RuntimeException("User with id: " + id + "not found");
        }
    }

    @Override
    public User save(User user) {
        log.debug("Saving user: " + user);
        return userRepository.save(user);
    }

    @Override
    public Set<User> findAll() {
        log.debug("Finding all users");
        return new HashSet<>(userRepository.findAll());
    }

    @Override
    public void delete(User user) {
        if (user == null) {
            log.error("User is null");
            throw new RuntimeException("User is null");
        }
        findById(user.getId());
        log.debug("Deleting user: " + user);
        userRepository.delete(user);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        log.debug("Deleting user with id: " + id);
        userRepository.deleteById(id);
    }


    @Override
    public User findByUsername(String username) {
        if (username == null) {
            log.error("Username is null");
            return null;
        }

        log.debug("Getting user with username: " + username);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            log.debug("User with following username not found: " + username);
            // TODO: 2019-06-22 create custom exception and handle error thourgh advice
            throw new RuntimeException("User with username: " + username + " not found");
        }
    }
}
