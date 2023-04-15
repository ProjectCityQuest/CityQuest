/**
 * The interface for all data feeding classes for the backend (persistent/volatile)
 */

package com.example.backend.db;

import com.example.backend.entity.User;

import java.util.List;

public interface DatabaseAccess {


    /**
     *
     * @return all current users
     */
    List<User> getAllUser();


    /**
     *
     * @param id of the user that is to be returned
     * @return a user object that matches the id
     */
    User getUserById(int id);


    /**
     *
     * @param email of the user that is to be returned
     * @return a user object that matches the email
     */
    User getUserByEmail(String email);


    /**
     *
     * @param user creates a user object
     */
    void createUser(User user);

    /**
     *
     * @param user deletes the given user
     */

    void deleteUser(User user);

    void changePassword(User user, String password);

    void submitRatings(int[] ratings);
}
