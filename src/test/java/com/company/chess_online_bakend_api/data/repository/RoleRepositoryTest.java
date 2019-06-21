package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.bootstrap.UserBootstrap;
import com.company.chess_online_bakend_api.data.model.Role;
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
class RoleRepositoryTest {

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
    public void testFindAdminRoleByDescription() throws Exception {
        Role role = roleRepository.findByDescription("ROLE_ADMIN");
        assertEquals("ROLE_ADMIN", role.getDescription());
    }

    @Test
    public void testFindUserRoleByDescription() throws Exception {
        Role role = roleRepository.findByDescription("ROLE_USER");
        assertEquals("ROLE_USER", role.getDescription());
    }

    @Test
    public void findByDescriptionNoMatch() throws Exception {
        Role role = roleRepository.findByDescription("Do not exists");
        assertNull(role);
    }
}