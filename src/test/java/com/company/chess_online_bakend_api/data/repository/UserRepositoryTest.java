package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.bootstrap.UserBootstrap;
import com.company.chess_online_bakend_api.data.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

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
        Optional<User> userOptional = userRepository.findByUsernameLike("ken123");
        assertTrue(userOptional.isPresent());
        assertEquals("ken123", userOptional.get().getUsername());
    }

    @Test
    public void findByUsernameNoMatch() throws Exception {
        Optional<User> userOptional = userRepository.findByUsernameLike("Do not exists");
        assertTrue(userOptional.isEmpty());
    }
}