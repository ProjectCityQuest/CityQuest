/**
 * This class implements the basic {@link com.example.backend.db.DatabaseAccess} interface
 * and uses a MySQL database to save the data persistently
 */

package com.example.backend.db;

import com.example.backend.dto.ErrorDto;
import com.example.backend.entity.CollectionEntry;
import com.example.backend.entity.PuzzlePiece;
import com.example.backend.entity.Spot;
import com.example.backend.entity.User;
import com.example.backend.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DatabaseAccessImplementation implements DatabaseAccess {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    /**
     * DB_URL is the url of the database
     * DB_USERNAME is the database users username
     * DB_PASSWORD is the database users password
     */
    public static String DB_URL;
    private final String DB_USERNAME = "root";
    private final String DB_PASSWORD = "password";

    /**
     * jdbcTemplate handles all queries and requests to the database
     */
    JdbcTemplate jdbcTemplate;

    /**
     * userList contains all current Users
     */
    private static final List<User> userList = new ArrayList<>();

    /**
     * creates a connection between the database and the backend
     */
    public DatabaseAccessImplementation() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * @see DatabaseAccess
     */
    @Override
    public List<User> getAllUser() {
        List<Map<String, Object>> users = jdbcTemplate.queryForList("SELECT * FROM Users;");

        for (Map<String, Object> currentUser : users) {
            int id = Integer.parseInt(currentUser.get("pk_id") + "");
            String username = currentUser.get("username") + "";
            String password = currentUser.get("password") + "";
            String email = currentUser.get("email") + "";
            boolean emailIsVerified;
            if (currentUser.get("email_is_verified") == null) {
                emailIsVerified = false;
            } else {
                emailIsVerified = (boolean) currentUser.get("email_is_verified");
            }

            User user = new User(username, email, password);
            user.setId(id);
            user.setEmailIsVerified(emailIsVerified);
            userList.add(user);
        }

        return userList;
    }

    /**
     * @see DatabaseAccess
     */
    @Override
    public User getUserById(int id) {
        String statement = "SELECT * FROM Users where pk_id = ?;";
        User user = jdbcTemplate.queryForObject(statement, new Object[]{id}, (rs, rowNum) -> {
            int pk_id = Integer.parseInt(rs.getInt("pk_id")+"");
            String username = rs.getString("username")+"";
            String password = rs.getString("password")+"";
            String email = rs.getString("email")+"";
            boolean emailIsVerified;
            if (rs.getString("email_is_verified") == null) {
                emailIsVerified = false;
            } else {
                emailIsVerified = Boolean.parseBoolean(rs.getString("email_is_verified"));
            }

            User newUser = new User(username, email, password);
            newUser.setId(pk_id);
            newUser.setEmailIsVerified(emailIsVerified);

            return newUser;
        });

        LOG.info("DB ACCESSED to retrieve User with id: '" + id + "'");

        return user;
    }

    /**
     * @see DatabaseAccess
     */
    public void createUser(User user) {
        String statement = "insert into Users (username, password, email) values (?, ?, ?);";
        Object[] params = new Object[]{user.getUsername(), user.getPassword(), user.getEmail()};
        jdbcTemplate.update(statement, params);

        String statement1 = "SELECT * FROM Users where username = ?;";
        Object[] params1 = {user.getUsername()};
        List<Map<String, Object>> users = jdbcTemplate.queryForList(statement1, params1);

        Map<String, Object> currentUser = users.get(0);

        int id = Integer.parseInt(currentUser.get("pk_id") + "");
        String username = currentUser.get("username") + "";
        String password = currentUser.get("password") + "";
        String email = currentUser.get("email") + "";
        User newUser = new User(username, email, password);
        newUser.setId(id);

        userList.add(newUser);

        LOG.info("User '" + username + "' has been created with ID: " + id);
    }

    /**
     * @see DatabaseAccess
     */

    public void deleteUser(User user) {
        String statement = "DELETE from Users where pk_id = ?";
        jdbcTemplate.update(statement, user.getId());

        userList.remove(user);

        LOG.info("User: '" + user.getUsername() + "' with Id: '" + user.getId() + "' has been deleted!");
    }

    public void changePassword(User user, String password) {
        String statement = "UPDATE Users SET password = ? WHERE pk_id = ?";
        Object[] params = new Object[]{password, user.getId()};
        jdbcTemplate.update(statement, params);

        User currentUser = UserServiceImpl.getUserById(user.getId());
        String oldPassword = currentUser.getPassword();
        currentUser.setPassword(password);

        LOG.info("Password of User:'" + user.getUsername() + "' with Id: '" + user.getId() + "' has been changed from '" + oldPassword + "' to '" + password + "'");
    }

    public void changeUsername(User user, String username) {
        String statement = "UPDATE Users SET username = ? WHERE pk_id = ?";
        Object[] params = new Object[]{username, user.getId()};
        jdbcTemplate.update(statement, params);

        User currentUser = UserServiceImpl.getUserById(user.getId());
        String oldUsername = currentUser.getUsername();
        currentUser.setUsername(username);

        LOG.info("Username of User with Id:'" + user.getId() + "' has been changed from '" + oldUsername + "' to '" + username + "'");
    }

    public void submitRatings(int[] ratings) {
        String statement = "insert into Bewertung (design, navigation, puzzle, sammelbuch) values (?, ?, ?, ?);";
        Object[] params = new Object[]{ratings[0], ratings[1], ratings[2], ratings[3]};
        jdbcTemplate.update(statement, params);

        LOG.info("A rating has been submitted {design: " + ratings[0] + ", navigation: " + ratings[1] + ", puzzle: " + ratings[2] + ", sammelbuch: " + ratings[3] + "}");
    }

    public String getProfilePicture(int id) {
        String statement = "SELECT profile_picture FROM Users where pk_id = ?;";
        String profilePicture = jdbcTemplate.queryForObject(statement, new Object[]{id}, (rs, rowNum) -> rs.getString("profile_picture"));

        if (profilePicture == null) {
            String defaultStatement = "SELECT profile_picture FROM DefaultData where pk_id = 1;";
            profilePicture = jdbcTemplate.queryForObject(defaultStatement, (rs, rowNum) -> rs.getString("profile_picture"));
        }

        LOG.info("DB ACCESSED to retrieve Profile Picture of user with id: '" + id + "'");

        return profilePicture;
    }

    public void changeProfilePicture(int id, String data) {
        String statement = "UPDATE Users SET profile_picture = ? WHERE pk_id = ?";
        Object[] params = new Object[]{data, id};
        jdbcTemplate.update(statement, params);

        User currentUser = UserServiceImpl.getUserById(id);

        LOG.info("Profile Picture of User: '" + currentUser.getUsername() + "' with Id:'" + id + "' has been changed");
    }

    public List<CollectionEntry> getAllCollectionEntries(int id) {
        List<CollectionEntry> collection = new LinkedList<>();

        String statement = "SELECT * FROM Sammelbucheintrag where fk_user_id = ?;";
        List<Map<String, Object>> entries = jdbcTemplate.queryForList(statement, new Object[]{id});

        for (Map<String, Object> currentEntry : entries) {
            int pk_id = Integer.parseInt(currentEntry.get("pk_id") + "");
            String timestamp = currentEntry.get("timestamp") + "";
            int locationId = Integer.parseInt(currentEntry.get("fk_location_id") + "");
            String location = "";
            String text = currentEntry.get("text") + "";
            String bild = currentEntry.get("bild") + "";

            String s = "SELECT name FROM Spot where pk_id = ?";
            Object[] p = new Object[]{locationId};
            List<Map<String, Object>> data = jdbcTemplate.queryForList(s, p);
            location = data.get(0).get("name")+"";

            CollectionEntry entry = new CollectionEntry(pk_id, timestamp, locationId, location, text, bild);

            collection.add(entry);
        }

        return collection;
    }

    public CollectionEntry getCollectionEntry(int entryId, int userId) {
        String statement = "SELECT * FROM Sammelbucheintrag where pk_id = ?;";
        CollectionEntry entry = jdbcTemplate.queryForObject(statement, new Object[]{entryId}, (rs, rowNum) -> {
            int locationID = Integer.parseInt(rs.getString("fk_location_id")+"");

            String s = "SELECT name FROM Spot where pk_id = ?";
            Object[] params = new Object[]{locationID};
            String locationName = jdbcTemplate.queryForObject(s, params, String.class);

            return new CollectionEntry(rs.getInt("pk_id"), rs.getString("timestamp"), locationID, locationName, rs.getString("text"), rs.getString("bild"));
        });

        return entry;
    }

    public List<Spot> getSpots(int userId) {
        List<Map<String, Object>> spots = jdbcTemplate.queryForList("SELECT * FROM Spot;");

        List<Spot> spotList = new LinkedList<>();

        for (Map<String, Object> currentSpot : spots) {
            int id = Integer.parseInt(currentSpot.get("pk_id") + "");
            String name = currentSpot.get("name") + "";
            double latitude = Double.parseDouble(currentSpot.get("latitude")+"");
            double longitude = Double.parseDouble(currentSpot.get("longitude") + "");
            String description = currentSpot.get("beschreibung")+"";

            String checkStatement = "SELECT max(pk_id) FROM userHatBesucht WHERE fk_user_id = ? AND fk_spot_id = ?;";
            Object[] checkParams = new Object[]{userId, id};
            Object items = jdbcTemplate.queryForObject(checkStatement, checkParams, Object.class);

            boolean discovered = items != null;


            Spot spot = new Spot(id, name, new double[] {longitude, latitude}, discovered, description);
            spotList.add(spot);
        }

        return spotList;
    }

    public boolean deleteEntry(int id) {
        String statement = "SELECT count(pk_id) FROM Sammelbucheintrag where pk_id = ?;";
        int entry = jdbcTemplate.queryForObject(statement, new Object[] {id}, Integer.class);

        if (entry == 0) return false;

        String deleteStatement = "DELETE FROM Sammelbucheintrag WHERE pk_id = ?";
        jdbcTemplate.update(deleteStatement, new Object[] {id});
        return true;
    }

    public List<PuzzlePiece> getPuzzle(int id, int pageIndex) {
        List<PuzzlePiece> puzzlePieces = new LinkedList<>();

        int from = 1 + 10 * (pageIndex-1);
        int to = pageIndex * 10;

        String statement = "SELECT * FROM Puzzle WHERE pk_id BETWEEN ? AND ?;";
        Object[] params = new Object[]{from, to};
        List<Map<String, Object>> puzzles = jdbcTemplate.queryForList(statement, params);

        for (Map<String, Object> current:puzzles) {
            String name = current.get("name") + "";
            int pos_col = Integer.parseInt(current.get("pos_col") + "");
            int pos_row = Integer.parseInt(current.get("pos_row") + "");
            String bild = "";

            String checkStatement = "SELECT max(pk_id) FROM userHatBesucht WHERE fk_user_id = ? AND fk_spot_id = (SELECT max(pk_id) FROM Spot where name = ?);";
            Object[] checkParams = new Object[]{id, name};
            Object items = jdbcTemplate.queryForObject(checkStatement, checkParams, Object.class);

            if (items != null) {
                bild = current.get("bild")+"";
            }

            String statement1 = "SELECT * FROM Spot where name = ?;";
            Object[] params1 = {name};
            List<Map<String, Object>> data = jdbcTemplate.queryForList(statement1, params1);

            Map<String, Object> current1 = data.get(0);

            PuzzlePiece puzzlePiece = new PuzzlePiece(Integer.parseInt(current1.get("pk_id")+""), name, bild, pos_col, pos_row);

            puzzlePieces.add(puzzlePiece);
        }

        return puzzlePieces;
    }

    public ResponseEntity<Object> collectPuzzlePiece(int userId, int spotId) {
        String statement1 = "SELECT max(pk_id) FROM Spot where pk_id = ?";
        Object[] params1 = new Object[]{spotId};
        Object items1 = jdbcTemplate.queryForObject(statement1, params1, Object.class);

        if (items1 == null) {
            return new ResponseEntity<>(new ErrorDto("Es gibt keinen spot mit dieser spotId"), HttpStatus.FORBIDDEN);
        }

        String statement2 = "SELECT max(pk_id) FROM userHatBesucht where fk_user_id = ? and fk_spot_id = ?";
        Object[] params2 = new Object[]{userId, spotId};
        Object items2 = jdbcTemplate.queryForObject(statement2, params2, Object.class);

        if (items2 != null) {
            return new ResponseEntity<>(new ErrorDto("Das Puzzleteil wurde bereits eingesammelt"), HttpStatus.PAYMENT_REQUIRED);
        }

        String statement3 = "INSERT INTO userHatBesucht (fk_user_id, fk_spot_id) VALUES (?, ?)";
        Object[] params3 = new Object[]{userId, spotId};
        jdbcTemplate.update(statement3, params3);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public boolean createEntry(int userId, CollectionEntry entry) {
        String statement1 = "SELECT max(pk_id) FROM Spot where pk_id = ?";
        Object[] params1 = new Object[]{entry.getLocationId()};
        Object items1 = jdbcTemplate.queryForObject(statement1, params1, Object.class);

        if (items1 == null) {
            return false;
        }

        String statement = "INSERT INTO Sammelbucheintrag (timestamp, fk_location_id, text, bild, fk_user_id) VALUES (?, ?, ?, ?, ?)";
        Object[] params = new Object[]{entry.getTimestamp(), entry.getLocationId(), entry.getText(), entry.getImage(), userId};
        jdbcTemplate.update(statement, params);

        return true;
    }
}
