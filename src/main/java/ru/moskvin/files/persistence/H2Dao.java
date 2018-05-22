package ru.moskvin.files.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class H2Dao {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public H2Dao(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS "
                + "tab_users(username VARCHAR(255) PRIMARY KEY, password VARCHAR(255), enabled BOOLEAN);");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS "
                + "authorities(username VARCHAR(255), authority  VARCHAR(255)," +
                " foreign key (username) references tab_users(username));");
    }
}
