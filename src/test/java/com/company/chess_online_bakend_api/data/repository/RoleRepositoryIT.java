/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.bootstrap.dev.UserBootstrap;
import com.company.chess_online_bakend_api.data.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RoleRepositoryIT {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        UserBootstrap userBootstrap = new UserBootstrap(userRepository, roleRepository, bCryptPasswordEncoder);
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