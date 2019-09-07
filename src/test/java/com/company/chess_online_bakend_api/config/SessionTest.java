/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.config;

import com.company.chess_online_bakend_api.bootstrap.dev.DeleteSessionsBootstrap;
import com.company.chess_online_bakend_api.bootstrap.dev.UserBootstrap;
import com.company.chess_online_bakend_api.controller.AuthenticationController;
import com.company.chess_online_bakend_api.data.repository.RoleRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class SessionTest {

    @Autowired
    DeleteSessionsBootstrap deleteSessionsBootstrap;

    @Autowired
    UserBootstrap userBootstrap;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        deleteSessionsBootstrap.run();
        userBootstrap.onApplicationEvent(null);

    }

    @AfterEach
    void tearDown() throws Exception {
        deleteSessionsBootstrap.run();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private List<String> getSessionIdsFromDatabase()
            throws SQLException {

        List<String> result = new ArrayList<>();
        ResultSet rs = getResultSet(
                "SELECT * FROM SPRING_SESSION");

        while (rs.next()) {
            result.add(rs.getString("SESSION_ID"));
        }
        return result;
    }

    private List<byte[]> getSessionAttributeBytesFromDb()
            throws SQLException {

        List<byte[]> result = new ArrayList<>();
        ResultSet rs = getResultSet(
                "SELECT * FROM SPRING_SESSION_ATTRIBUTES");

        while (rs.next()) {
            result.add(rs.getBytes("ATTRIBUTE_BYTES"));
        }
        return result;
    }

    private ResultSet getResultSet(String sql)
            throws SQLException {

        var properties = new Properties();
        properties.put("user", "root");

        Connection conn = DriverManager
                .getConnection("jdbc:mysql://localhost/chess_online_dev?useSSL=false", properties);
        Statement stat = conn.createStatement();
        return stat.executeQuery(sql);
    }

    @Test
    void whenMYSQLIsQueried_thenSessionInfoIsEmpty() throws SQLException {

        assertEquals(
                0, getSessionIdsFromDatabase().size());
        assertEquals(
                0, getSessionAttributeBytesFromDb().size());
    }

    @Test
    void whenServerIsQueried_thenSessionIsCreated() throws SQLException {

        assertThat(this.testRestTemplate.postForObject("http://localhost:" + port +
                AuthenticationController.BASE_URL + "login?username=" + UserBootstrap.USER_USERNAME +
                "&password=" + UserBootstrap.USER_PASSWORD, "", String.class))
                .isNull();

        assertEquals(1, getSessionIdsFromDatabase().size());
    }
}

