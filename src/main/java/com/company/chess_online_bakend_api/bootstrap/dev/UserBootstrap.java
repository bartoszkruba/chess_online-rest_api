/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.bootstrap.dev;

import com.company.chess_online_bakend_api.data.model.Role;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.RoleRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Profile("dev")
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

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "password";
    public static final String ADMIN_EMAIL = "admin@email.com";

    public static final String USER_USERNAME = "user";
    public static final String USER_PASSWORD = "password";
    public static final String USER_EMAIL = "user@email.com";

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadRoles();
        log.info("Roles loaded: [" + ROLE_ADMIN + ", " + ROLE_USER + "]");
        loadUsers();
        log.debug("Users loaded: " + userRepository.count());
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
    }

    private void loadUsers() {
        Role adminRole = roleRepository.findByDescription(ROLE_ADMIN);
        if (adminRole == null) {
            log.error(ROLE_ADMIN + " not found");
            throw new RuntimeException("ROLE_ADMIN not found");
        }

        Role userRole = roleRepository.findByDescription(ROLE_USER);
        if (userRole == null) {
            log.error(ROLE_USER + " not found");
            throw new RuntimeException("ROLE_USER not found");
        }


        if (userRepository.findByUsernameLike(ADMIN_USERNAME).isEmpty()) {
            User adminUser = User.builder()
                    .firstName("Kirk")
                    .lastName("Kennedy")
                    .username(ADMIN_USERNAME)
                    .email(ADMIN_EMAIL)
                    .password(encoder.encode(ADMIN_PASSWORD)).build().addRole(adminRole);
            userRepository.save(adminUser);
            log.info("Loaded admin - username: " + ADMIN_USERNAME +
                    ", password: " + ADMIN_PASSWORD +
                    ", email: " + ADMIN_EMAIL);
        }

        if (userRepository.findByUsernameLike(USER_USERNAME).isEmpty()) {
            User normalUser = User.builder()
                    .firstName("Carl")
                    .lastName("Soto")
                    .username(USER_USERNAME)
                    .email(USER_EMAIL)
                    .password(encoder.encode(USER_PASSWORD)).build().addRole(userRole);
            userRepository.save(normalUser);
            log.info("Loaded user - username: " + USER_USERNAME +
                    ", password: " + USER_PASSWORD +
                    ", email: " + USER_EMAIL);
        }
    }

}
