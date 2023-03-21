package com.example.backend.db;

import com.example.backend.entity.User;
import com.example.backend.service.UserServiceImpl;
import com.example.backend.util.Strings;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessTestImplementation implements DatabaseAccess {
    public static List<User> userList = new ArrayList<>();

    public DatabaseAccessTestImplementation() {
        User user = new User("Daniel Pillwein", "dani@gmail.com", "gutesPasswort");
        user.setId(0);
        user.setToken(Strings.generateToken());
        user.setEmailIsVerified(true);
        userList.add(user);
        user = new User("Lukas Schodl", "luki@gmail.com", "besseresPasswort");
        user.setId(1);
        user.setToken(Strings.generateToken());
        user.setEmailIsVerified(true);
        userList.add(user);
        user = new User("Markus Wizany", "markus@gmail.com", "schlechtesPasswort");
        user.setId(2);
        user.setToken(Strings.generateToken());
        user.setEmailIsVerified(false);
        userList.add(user);
        System.out.println(userList);
    }

    @Override
    public List<User> getAllUser() {
        return userList;
    }

    @Override
    public User getUserById(int id) {
        return userList.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return userList.stream().filter(x -> x.getEmail().equals(email)).findFirst().orElse(null);
    }

    @Override
    public void createUser(User user) {
        user.setId(UserServiceImpl.getUserList().size());
        userList.add(user);
    }
}
