package com.example.backend.service;

import com.example.backend.db.DatabaseAccess;
import com.example.backend.db.DatabaseAccessImplementation;
import com.example.backend.db.DatabaseAccessTestImplementation;
import com.example.backend.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl {
    private static List<User> userList = new ArrayList<>();
    private DatabaseAccess DATABASE;

    public UserServiceImpl() {
        DATABASE = new DatabaseAccessTestImplementation();
        userList = DATABASE.getAllUser();
    }

    public static List<User> getUserList() {
        return userList;
    }

    public static void addUser(User user) {
        userList.add(user);
    }

    public static User getUserByName(String name) {
        return getUserList().stream().filter(x -> x.getUsername().equals(name)).findFirst().orElse(null);
    }

    public static User getUserByEmail(String email) {
        return getUserList().stream().filter(x -> x.getEmail().equals(email)).findFirst().orElse(null);
    }

    public static User getUserByToken(String token) {
        return getUserList().stream().filter(x -> x.getToken() != null).filter(x -> x.getToken().equals(token)).findFirst().orElse(null);
    }

    public static User getUserById(int id) {
        return getUserList().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }
}
