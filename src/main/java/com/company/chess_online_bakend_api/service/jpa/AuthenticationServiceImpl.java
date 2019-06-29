package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.model.Role;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Collection<String> getRolesForUser(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        // TODO: 2019-06-29 create custom exception for no user found
        // TODO: 2019-06-29 create advice for handling exception
        if (userOptional.isEmpty()) {
            throw new RuntimeException();
        }

        return userOptional.get()
                .getRoles().stream()
                .map(Role::getAuthority).collect(Collectors.toSet());
    }
}
