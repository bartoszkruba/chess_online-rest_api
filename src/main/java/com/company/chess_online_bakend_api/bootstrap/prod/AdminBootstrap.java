/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.bootstrap.prod;

import com.company.chess_online_bakend_api.data.model.Role;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.RoleRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile("prod")
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    @Value("${admin-username}")
    public String ADMIN_USERNAME;
    @Value("${admin-password}")
    public String ADMIN_PASSWORD;
    @Value("${admin-email}")
    public String ADMIN_EMAIL;

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
        Role adminRole = roleRepository.findByDescription(ROLE_ADMIN);

        if (adminRole == null) {
            log.error(ROLE_ADMIN + " not found");
            throw new RuntimeException(ROLE_ADMIN + " not found");
        }

        if (userRepository.findByUsernameLike(ADMIN_USERNAME).isEmpty()) {
            User adminUser = User.builder()
                    .firstName("")
                    .lastName("")
                    .username(ADMIN_USERNAME)
                    .email(ADMIN_EMAIL)
                    .password(encoder.encode(ADMIN_PASSWORD)).build().addRole(adminRole);
            userRepository.save(adminUser);
        }

        log.info("Loaded admin - username: " + ADMIN_USERNAME +
                ", password: " + ADMIN_PASSWORD +
                ", email: " + ADMIN_EMAIL);
    }

    private void loadRoles() {
        if (roleRepository.findByDescription(ROLE_USER) == null) {
            Role role1 = Role.builder().description(ROLE_USER).build();
            roleRepository.save(role1);
        }
        if (roleRepository.findByDescription(ROLE_ADMIN) == null) {
            Role role2 = Role.builder().description(ROLE_ADMIN).build();
            roleRepository.save(role2);
        }

        log.info("Loaded roles: [" + ROLE_ADMIN + ", " + ROLE_USER + "]");
    }
}
