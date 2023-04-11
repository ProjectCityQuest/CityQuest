/**
 * This is the main class to manage the users
 */

package com.example.backend.service;

import com.example.backend.db.DatabaseAccess;
import com.example.backend.db.DatabaseAccessImplementation;
import com.example.backend.db.DatabaseAccessTestImplementation;
import com.example.backend.entity.User;

import java.util.*;

public class UserServiceImpl {
    private static List<User> userList = new ArrayList<>();
    private static DatabaseAccess DATABASE;

    /**
     * This map contains the timestamp of when the verification mail got sent
     *
     * @key Token of the email Verification
     * @value timestamp as a Date Object
     */
    private static final Map<String, Date> pendingEmailVerificationsDate = new HashMap<>();

    /**
     * This map contains the email address of the user which received a verification email
     *
     * @key Token of the email Verification
     * @value email address of the user
     */
    private static final Map<String, String> pendingEmailVerificationsEmail = new HashMap<>();

    /**
     * This map contains the email address of the user which wants to reset its password
     *
     * @key Token of the password reset
     * @value email address of the user
     */
    private static final Map<String, String> pendingPasswordResetEmail = new HashMap<>();

    /**
     * This map contains the timestamp of when the password reset mail got sent
     *
     * @key Token of the password reset
     * @value timestamp as a Date Object
     */
    private static final Map<String, Date> pendingPasswordResetDate = new HashMap<>();

    /**
     * This contructor creates loads the already existing users and prints logs into console
     */

    public UserServiceImpl() {
        DATABASE = new DatabaseAccessImplementation();
        userList = DATABASE.getAllUser();

        System.out.println(userList);
    }

    /**
     * Return a List of all users
     */

    public static List<User> getUserList() {
        return userList;
    }

    /**
     * Returns the list of current email Verification Dates
     */

    public static Map<String, Date> getPendingEmailVerificationsDate() {
        return pendingEmailVerificationsDate;
    }

    /**
     * Return the list of current email verification addresses
     */

    public static Map<String, String> getPendingEmailVerificationsEmail() {return pendingEmailVerificationsEmail;}

    /**
     * removes a pending email verification
     *
     * @param id the token of the verification email that is to be removed
     */

    public static void removePendingEmailVerification(String id) {
        pendingEmailVerificationsDate.remove(id);
        pendingEmailVerificationsEmail.remove(id);
    }

    /**
     * adds a new email verification
     *
     * @param id the token of the verification email that is to be removed
     * @param email the email address of the user that received a verification email
     */

    public static void addPendingEmail(String id, String email) {
        pendingEmailVerificationsDate.put(id, new Date());
        pendingEmailVerificationsEmail.put(id, email);
    }

    /**
     * adds a new user to the List of current user
     * creates a row for the user in the database
     *
     * @param user User object of the user that is to be created
     */

    public static void addUser(User user) {
        DATABASE.createUser(user);
    }

    /**
     * returns a User object of the user that matches the name
     *
     * @param name Is the name of the user that is to be returned
     */

    public static User getUserByName(String name) {
        return getUserList().stream().filter(x -> x.getUsername().equals(name)).findFirst().orElse(null);
    }

    /**
     * returns a User object of the user that matches the email
     *
     * @param email Is the email of the user that is to be returned
     */

    public static User getUserByEmail(String email) {
        return getUserList().stream().filter(x -> x.getEmail().equals(email)).findFirst().orElse(null);
    }

    /**
     * returns a User object of the user that matches the token
     *
     * @param token Is the token of the user that is to be returned
     */

    public static User getUserByToken(String token) {
        return getUserList().stream().filter(x -> x.getToken() != null).filter(x -> x.getToken().equals(token)).findFirst().orElse(null);
    }

    /**
     * returns a User object of the user that matches the id
     *
     * @param id Is the id of the user that is to be returned
     */

    public static User getUserById(int id) {
        return getUserList().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    /**
     *
     * @param id is the token that was generated and sent with the email to reset the password
     * @param email of the user
     */

    public static void addPendingPasswordReset(String id, String email) {
        pendingPasswordResetEmail.put(id, email);
        pendingPasswordResetDate.put(id, new Date());
    }

    /**
     *
     * @param id is the token that was generated and sent with the email to reset the password
     */

    public static void removePendingPasswordReset(String id) {
        pendingPasswordResetDate.remove(id);
        pendingPasswordResetEmail.remove(id);
    }

    /**
     *
     * @return the Map of pending password reset dates
     */

    public static Map<String, Date> getPendingPasswordResetDate() {
        return pendingPasswordResetDate;
    }

    /**
     *
     * @return the Map of pending password reset emails
     */

    public static Map<String, String> getPendingPasswordResetEmail() {
        return pendingPasswordResetEmail;
    }
}
