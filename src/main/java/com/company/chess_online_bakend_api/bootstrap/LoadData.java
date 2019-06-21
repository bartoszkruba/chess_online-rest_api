package com.company.chess_online_bakend_api.bootstrap;

import com.company.chess_online_bakend_api.data.model.Role;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.RoleRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoadData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public LoadData(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.debug("Loading user roles");
        loadRoles();
        log.debug("Loading users");
        loadUsers();
    }

    private void loadRoles() {
        Role role1 = Role.builder().description("ROLE_USER").build();
        roleRepository.save(role1);
        Role role2 = Role.builder().description("ROLE_ADMIN").build();
        roleRepository.save(role2);
    }

    private void loadUsers() throws Exception {
        Role adminRole = roleRepository.findByDescription("ROLE_ADMIN");
        if (adminRole == null) {
            log.error("ROLE_ADMIN not found");
            throw new Exception("ROLE_ADMIN not found");
        }

        Role userRole = roleRepository.findByDescription("ROLE_USER");
        if (userRole == null) {
            log.error("ROLE_USER not found");
            throw new Exception("ROLE_USER not found");
        }

        User adminUser = User.builder()
                .firstName("Kirk")
                .lastName("Kennedy")
                .username("ken123")
                .password("devo").build().addRole(adminRole);
        userRepository.save(adminUser);

        User normalUser = User.builder()
                .firstName("Carl")
                .lastName("Soto")
                .username("carl69")
                .password("tyler1").build().addRole(userRole);
        userRepository.save(normalUser);
    }
}
