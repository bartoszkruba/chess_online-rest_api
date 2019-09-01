/*
 * 8/31/19, 7:17 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.config.socket;

import com.company.chess_online_bakend_api.data.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WebSocketAuthenticatorService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSocketAuthenticatorService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public UsernamePasswordAuthenticationToken getAuthenticated(final String username, final String password)
            throws AuthenticationException {

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            String principal = UUID.randomUUID().toString();
            log.debug("Credential headers either null or empty, assigning principal: " + principal);
            return new UsernamePasswordAuthenticationToken(principal, null);
        }

        log.debug("Fetching user from database: " + username);
        var user = userRepository.findByUsernameLike(username)
                .orElseThrow(() -> {
                    log.debug("User not found");
                    return new AuthenticationCredentialsNotFoundException("User not found");
                });

        log.debug("Comparing passwords");
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {

            var authorities = user.getRoles().stream()
                    .map(r -> (GrantedAuthority) r::getDescription).collect(Collectors.toList());

            log.debug("Assigning principal: " + username);
            log.debug("Assigning authorities: " + authorities);
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        } else {
            log.debug("Passwords doesn't match");
            throw new AuthenticationCredentialsNotFoundException("Invalid Password");
        }
    }
}
