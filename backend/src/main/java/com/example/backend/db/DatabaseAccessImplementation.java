/**
 * This class implements the basic {@link com.example.backend.db.DatabaseAccess} interface
 * and uses a MySQL database to save the data persistently
 */

package com.example.backend.db;

import com.example.backend.entity.CollectionEntry;
import com.example.backend.entity.Spot;
import com.example.backend.entity.User;
import com.example.backend.service.UserServiceImpl;
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
            String location = currentEntry.get("location") + "";
            String text = currentEntry.get("text") + "";
            String bild = currentEntry.get("bild") + "";

            CollectionEntry entry = new CollectionEntry(pk_id, timestamp, location, text, bild);

            collection.add(entry);
        }

        return collection;
    }

    public CollectionEntry getCollectionEntry(int entryId, int userId) {
        String statement = "SELECT * FROM Sammelbucheintrag where pk_id = ?;";
        CollectionEntry entry = jdbcTemplate.queryForObject(statement, new Object[]{entryId}, (rs, rowNum) -> new CollectionEntry(rs.getInt("pk_id"), rs.getString("timestamp"), rs.getString("location"), rs.getString("text"), rs.getString("bild")));

        return entry;
    }

    public List<Spot> getSpots() {
        List<Map<String, Object>> spots = jdbcTemplate.queryForList("SELECT * FROM Spot;");

        List<Spot> spotList = new LinkedList<>();

        for (Map<String, Object> currentSpot : spots) {
            int id = Integer.parseInt(currentSpot.get("pk_id") + "");
            String name = currentSpot.get("name") + "";
            double latitude = Double.parseDouble(currentSpot.get("latitude")+"");
            double longitude = Double.parseDouble(currentSpot.get("longitude") + "");
            String description = currentSpot.get("description")+"";

            Spot spot = new Spot(id, name, new double[] {longitude, latitude}, description);
            spotList.add(spot);
        }

        return spotList;
    }
}
