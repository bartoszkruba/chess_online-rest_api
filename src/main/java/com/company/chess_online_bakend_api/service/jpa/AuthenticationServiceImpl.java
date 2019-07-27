/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.converter.command.user.UserCommandToUser;
import com.company.chess_online_bakend_api.data.converter.command.user.UserToUserCommand;
import com.company.chess_online_bakend_api.data.model.Role;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.RoleRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.exception.UserNotFoundException;
import com.company.chess_online_bakend_api.service.AuthenticationService;
import com.company.chess_online_bakend_api.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserCommandToUser userCommandToUser;
    private final UserToUserCommand userToUserCommand;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserCommandToUser userCommandToUser, UserToUserCommand
            userToUserCommand, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userCommandToUser = userCommandToUser;
        this.userToUserCommand = userToUserCommand;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Collection<String> getRolesForUser(String username) {
        Optional<User> userOptional = userRepository.findByUsernameLike(username);

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
    @Transactional
    public UserCommand registerNewUser(UserCommand userCommand) {

        User user = userCommandToUser.convert(userCommand);

        user.setUsername(StringUtils.capitalizeFirstLetter(user.getUsername().toLowerCase()));
        user.setId(null);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByDescription("ROLE_USER");

        if (role == null) {
            log.error("Could not fetch role with description \"ROLE_USER\"");
            throw new RuntimeException("Could not fetch role with description \"ROLE_USER\"");
        }

        user.addRole(role);

        log.debug("Registering new user: " + user);

        return userToUserCommand.convert(userRepository.save(user));
    }
}
