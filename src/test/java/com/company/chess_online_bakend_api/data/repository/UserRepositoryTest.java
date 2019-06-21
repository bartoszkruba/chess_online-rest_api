package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.bootstrap.UserBootstrap;
import com.company.chess_online_bakend_api.data.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();
        userRepository.deleteAll();

        UserBootstrap userBootstrap = new UserBootstrap(userRepository, roleRepository);
        userBootstrap.onApplicationEvent(null);
    }

    @Test
    public void testFindByDescription() throws Exception {
        User user = userRepository.findByUsername("ken123");
        assertEquals("ken123", user.getUsername());
    }

    @Test
    public void findByDescriptionNoMatch() throws Exception {
        User user = userRepository.findByUsername("Do not exists");
        assertNull(user);
    }
}