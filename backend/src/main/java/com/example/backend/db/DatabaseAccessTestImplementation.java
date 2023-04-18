/**
 * This class implements the basic {@link com.example.backend.db.DatabaseAccess} interface
 * and is used for test purposes
 * All data using this class is volatile
 */

package com.example.backend.db;

import com.example.backend.entity.User;
import com.example.backend.service.UserServiceImpl;
import com.example.backend.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessTestImplementation implements DatabaseAccess {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    /**
     * userList contains all current Users
     */
    public static List<User> userList = new ArrayList<>();

    /**
     * creates test data for testing purposes
     */
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
        user = new User("Luki", "9137@htl.rennweg.at", "testtest");
        user.setId(3);
        user.setToken(Strings.generateToken());
        user.setEmailIsVerified(true);
        userList.add(user);
    }

    /**
     * @see DatabaseAccess
     */
    @Override
    public List<User> getAllUser() {
        return userList;
    }

    /**
     * @see DatabaseAccess
     */
    @Override
    public User getUserById(int id) {
        return userList.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    /**
     * @see DatabaseAccess
     */
    @Override
    public User getUserByEmail(String email) {
        return userList.stream().filter(x -> x.getEmail().equals(email)).findFirst().orElse(null);
    }

    /**
     * @see DatabaseAccess
     */
    @Override
    public void createUser(User user) {
        user.setId(UserServiceImpl.getUserList().size());
        userList.add(user);
    }

    /**
     *
     * @see DatabaseAccess
     */

    public void deleteUser(User user) {
        userList.remove(user);
    }

    public void changePassword(User user, String password) {
        UserServiceImpl.getUserById(user.getId()).setPassword(password);
    }

    public void submitRatings(int[] ratings) {
        LOG.info("A rating has been submitted {design: "+ratings[0]+", navigation: "+ratings[1]+", puzzle: " + ratings[2] + ", sammelbuch: " + ratings[3] + "}");
    }
}
