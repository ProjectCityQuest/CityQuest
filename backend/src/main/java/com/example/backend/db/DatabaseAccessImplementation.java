package com.example.backend.db;

import com.example.backend.entity.User;
import com.mysql.cj.protocol.Resultset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class DatabaseAccessImplementation implements DatabaseAccess {
    private String DB_URL;
    private String DB_USERNAME;
    private String DB_PASSWORD;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public DatabaseAccessImplementation() {
        jdbcTemplate.execute("SELECT * FROM User");
    }

    @Override
    public List<User> getAllUser() {
        return null;
    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }
}
