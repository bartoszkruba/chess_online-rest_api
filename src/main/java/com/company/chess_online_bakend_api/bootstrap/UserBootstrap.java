package com.company.chess_online_bakend_api.bootstrap;

import com.company.chess_online_bakend_api.data.model.Role;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.RoleRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class UserBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserBootstrap(UserRepository userRepository, RoleRepository roleRepository,
                         BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
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

    private void loadUsers() {
        Role adminRole = roleRepository.findByDescription("ROLE_ADMIN");
        if (adminRole == null) {
            log.error("ROLE_ADMIN not found");
            throw new RuntimeException("ROLE_ADMIN not found");
        }

        Role userRole = roleRepository.findByDescription("ROLE_USER");
        if (userRole == null) {
            log.error("ROLE_USER not found");
            throw new RuntimeException("ROLE_USER not found");
        }

        User adminUser = User.builder()
                .firstName("Kirk")
                .lastName("Kennedy")
                .username("ken123")
                .email("ken123@email.com")
                .password(encoder.encode("devo")).build().addRole(adminRole);
        userRepository.save(adminUser);

        User normalUser = User.builder()
                .firstName("Carl")
                .lastName("Soto")
                .username("carl69")
                .email("carl69@email.com")
                .password(encoder.encode("tyler1")).build().addRole(userRole);
        userRepository.save(normalUser);
    }

}
