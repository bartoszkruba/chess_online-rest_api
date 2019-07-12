package com.company.chess_online_bakend_api.bootstrap.prod;

import com.company.chess_online_bakend_api.data.model.Role;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.RoleRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
@Slf4j
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public AdminBootstrap(UserRepository userRepository, RoleRepository roleRepository,
                          BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        loadRoles();
        loadAdmin();
    }

    private void loadAdmin() {
        Role adminRole = roleRepository.findByDescription("ROLE_ADMIN");

        if (adminRole == null) {
            log.error("ROLE_ADMIN not found");
            throw new RuntimeException("ROLE_ADMIN not found");
        }

        if (userRepository.findByUsernameLike("admin").isEmpty()) {
            User adminUser = User.builder()
                    .firstName("")
                    .lastName("")
                    .username("admin")
                    .email("admin@email.com")
                    .password(encoder.encode("password")).build().addRole(adminRole);
            userRepository.save(adminUser);
        }

        log.info("Loaded admin");
    }

    private void loadRoles() {
        if (roleRepository.findByDescription("ROLE_USER") == null) {
            Role role1 = Role.builder().description("ROLE_USER").build();
            roleRepository.save(role1);
        }
        if (roleRepository.findByDescription("ROLE_ADMIN") == null) {
            Role role2 = Role.builder().description("ROLE_ADMIN").build();
            roleRepository.save(role2);
        }

        log.info("Loaded roles: " + roleRepository.count());
    }
}
