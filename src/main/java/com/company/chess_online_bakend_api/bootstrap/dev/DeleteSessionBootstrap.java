

/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:10 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.bootstrap.dev;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


@Slf4j
@Component
@Profile("dev")
public class DeleteSessionBootstrap implements CommandLineRunner {

    @Value("${spring.datasource.url}")
    public String URL;
    @Value("${spring.datasource.username}")
    public String USERNAME;
    @Value("${spring.datasource.password}")
    public String PASSWORD;

    @Override
    public void run(String... args) throws Exception {
        deleteAllSessions();
    }

    private void deleteAllSessions() throws SQLException {
        log.info("Deleting old sessions.");

        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        Statement statement = conn.createStatement();
        statement.execute("DELETE FROM SPRING_SESSION");
        statement.execute("DELETE FROM SPRING_SESSION_ATTRIBUTES");

        conn.close();
        log.info("All sessions deleted");
    }
}
