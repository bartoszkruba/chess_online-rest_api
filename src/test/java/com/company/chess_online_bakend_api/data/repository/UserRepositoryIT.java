/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.bootstrap.dev.UserBootstrap;
import com.company.chess_online_bakend_api.data.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("dev")
class UserRepositoryIT {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();
        userRepository.deleteAll();

        UserBootstrap userBootstrap = new UserBootstrap(userRepository, roleRepository, bCryptPasswordEncoder);
        userBootstrap.onApplicationEvent(null);
    }

    @Test
    public void testFindByUsername() throws Exception {
        Optional<User> userOptional = userRepository.findByUsernameLike(UserBootstrap.USER_USERNAME);
        assertTrue(userOptional.isPresent());
        assertEquals(UserBootstrap.USER_USERNAME, userOptional.get().getUsername());
    }

    @Test
    public void findByUsernameNoMatch() throws Exception {
        Optional<User> userOptional = userRepository.findByUsernameLike("Do not exists");
        assertTrue(userOptional.isEmpty());
    }
}