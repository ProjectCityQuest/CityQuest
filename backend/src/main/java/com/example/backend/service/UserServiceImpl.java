package com.example.backend.service;

import com.example.backend.db.DatabaseAccess;
import com.example.backend.db.DatabaseAccessTestImplementation;
import com.example.backend.entity.User;
import com.example.backend.util.Strings;

import java.util.*;

public class UserServiceImpl {
    private static List<User> userList = new ArrayList<>();
    private DatabaseAccess DATABASE;
    private static Map<String, Date> pendingEmailVerificationsDate = new HashMap<>();
    private static Map<String, String> pendingEmailVerificationsEmail = new HashMap<>();

    public UserServiceImpl() {
        DATABASE = new DatabaseAccessTestImplementation();
        userList = DATABASE.getAllUser();

        pendingEmailVerificationsDate.put(Strings.generateToken(24), new Date());
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MINUTE, -20);
        Date currentDatePlusOne = c.getTime();
        pendingEmailVerificationsDate.put(Strings.generateToken(24), currentDatePlusOne);

        System.out.println(pendingEmailVerificationsDate);

        System.out.println(userList);
    }

    public static List<User> getUserList() {
        return userList;
    }

    public static Map<String, Date> getPendingEmailVerificationsDate() {
        return pendingEmailVerificationsDate;
    }

    public static Map<String, String> getPendingEmailVerificationsEmail() {return pendingEmailVerificationsEmail;}

    public static void removePendingEmailVerification(String id) {
        pendingEmailVerificationsDate.remove(id);
        pendingEmailVerificationsEmail.remove(id);
    }

    public static void addPendingEmail(String id, String email) {
        pendingEmailVerificationsDate.put(id, new Date());
        pendingEmailVerificationsEmail.put(id, email);
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
