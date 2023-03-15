package com.example.backend.service;

import com.example.backend.db.DatabaseAccess;
import com.example.backend.db.DatabaseAccessImplementation;
import com.example.backend.db.DatabaseAccessTestImplementation;
import com.example.backend.entity.User;
import com.example.backend.util.Strings;

import java.util.*;

public class UserServiceImpl {
    private static List<User> userList = new ArrayList<>();
    private DatabaseAccess DATABASE;
    private static Map<String, Date> pendingEmailVerifications = new HashMap<>();

    public UserServiceImpl() {
        DATABASE = new DatabaseAccessTestImplementation();
        userList = DATABASE.getAllUser();

        pendingEmailVerifications.put(Strings.generateToken(24), new Date());
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MINUTE, -20);
        Date currentDatePlusOne = c.getTime();
        pendingEmailVerifications.put(Strings.generateToken(24), currentDatePlusOne);

        System.out.println(pendingEmailVerifications);

        System.out.println(userList);
    }

    public static List<User> getUserList() {
        return userList;
    }

    public static Map<String, Date> getPendingEmailVerifications() {
        return pendingEmailVerifications;
    }

    public static void removePendingEmailVerification(String id) {

    }

    public static void addPendingEmail(String id) {
        pendingEmailVerifications.put(id, new Date());
        System.out.println(pendingEmailVerifications);
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
