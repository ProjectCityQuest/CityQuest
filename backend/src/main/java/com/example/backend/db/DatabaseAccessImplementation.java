package com.example.backend.db;

import com.example.backend.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.ResultSet;
import java.util.*;

public class DatabaseAccessImplementation implements DatabaseAccess {
    private String DB_URL = "jdbc:mysql://localhost:3306/cityquest";
    private String DB_USERNAME = "root";
    private String DB_PASSWORD = "";

    JdbcTemplate jdbcTemplate;

    public DatabaseAccessImplementation() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<User> getAllUser() {
        List<Map<String, Object>> users = jdbcTemplate.queryForList("SELECT * FROM User");
        System.out.println(users);

        List<User> userList = new ArrayList<>();

        for (Map<String, Object> currentUser :users) {
            int id = Integer.parseInt(currentUser.get("pk_id")+"");
            String username = currentUser.get("username")+"";
            String password = currentUser.get("password")+"";
            String email = currentUser.get("email")+"";
            String token = currentUser.get("token")+"";
            Date token_expiration_date = (Date)currentUser.get("token_expiration_date");

            User user = new User(username, email, password);
            user.setId(id);
            user.setToken(token);
            userList.add(user);

        }

        return userList;
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
