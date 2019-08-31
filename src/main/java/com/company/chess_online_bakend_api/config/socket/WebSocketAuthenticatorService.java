/*
 * 8/31/19, 7:17 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.config.socket;

import com.company.chess_online_bakend_api.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Component
public class WebSocketAuthenticatorService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSocketAuthenticatorService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(final String username, final String password)
            throws AuthenticationException {

        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Username was null or empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Password was null or empty.");
        }

        var user = userRepository.findByUsernameLike(username)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User not found"));
        if (user.getPassword().equals(bCryptPasswordEncoder.encode(password))) {

            var authorites = user.getRoles().stream()
                    .map(r -> (GrantedAuthority) r::getDescription).collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken(username, null, authorites);
        } else {
            throw new AuthenticationCredentialsNotFoundException("Invalid Password");
        }
    }
}
