/**
 * The interface for all data feeding classes for the backend (persistent/volatile)
 */

package com.example.backend.db;

import com.example.backend.entity.CollectionEntry;
import com.example.backend.entity.PuzzlePiece;
import com.example.backend.entity.Spot;
import com.example.backend.entity.User;
import org.springframework.http.ResponseEntity;

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
     * @param user creates a user object
     */
    void createUser(User user);

    /**
     *
     * @param user deletes the given user
     */

    void deleteUser(User user);

    void changePassword(User user, String password);

    void changeUsername(User user, String username);

    void submitRatings(int[] ratings);

    /**
     *
     * @param id of the profile pictures user
     */
    String getProfilePicture(int id);

    /**
     *
     * @param id of the user which changes his image
     * @param data contains base64 coded data of the profile picture
     */
    void changeProfilePicture(int id, String data);

    List<CollectionEntry> getAllCollectionEntries(int id);

    CollectionEntry getCollectionEntry(int entryId, int userId);

    boolean deleteEntry(int id);

    List<Spot> getSpots();

    List<PuzzlePiece> getPuzzle(int id, int pageIndex);

    ResponseEntity<Object> collectPuzzlePiece(int userId, int spotId);

    boolean createEntry(int userId, CollectionEntry entry);
}
