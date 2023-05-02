/**
 * This class implements the basic {@link com.example.backend.db.DatabaseAccess} interface
 * and uses a MySQL database to save the data persistently
 */

package com.example.backend.db;

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

        for (Map<String, Object> currentUser :users) {
            int id = Integer.parseInt(currentUser.get("pk_id")+"");
            String username = currentUser.get("username")+"";
            String password = currentUser.get("password")+"";
            String email = currentUser.get("email")+"";
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
        return null;
    } // TODO: implement

    /**
     * @see DatabaseAccess
     */
    @Override
    public User getUserByEmail(String email) {
        return null;
    } // TODO: implement

    /**
     * @see DatabaseAccess
     */
    public void createUser(User user) {
        String statement = "insert into Users (username, password, email) values (?, ?, ?);";
        Object[] params = new Object[] {user.getUsername(), user.getPassword(), user.getEmail()};
        jdbcTemplate.update(statement, params);

        String statement1 = "SELECT * FROM Users where username = ?;";
        Object[] params1 = {user.getUsername()};
        List<Map<String, Object>> users = jdbcTemplate.queryForList(statement1, params1);

        Map<String, Object> currentUser = users.get(0);

        int id = Integer.parseInt(currentUser.get("pk_id")+"");
        String username = currentUser.get("username")+"";
        String password = currentUser.get("password")+"";
        String email = currentUser.get("email")+"";
        User newUser = new User(username, email, password);
        newUser.setId(id);

        userList.add(newUser);

        LOG.info("User '" + username + "' has been created with ID: "+ id);
    }

    /**
     *
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
        Object[] params = new Object[] {password, user.getId()};
        jdbcTemplate.update(statement, params);

        User currentUser = UserServiceImpl.getUserById(user.getId());
        String oldPassword = currentUser.getPassword();
        currentUser.setPassword(password);

        LOG.info("Password of User:'" + user.getUsername() + "' with Id: '" + user.getId() + "' has been changed from '" + oldPassword + "' to '" + password + "'");
    }

    public void changeUsername(User user, String username) {
        String statement = "UPDATE Users SET username = ? WHERE pk_id = ?";
        Object[] params = new Object[] {username, user.getId()};
        jdbcTemplate.update(statement, params);

        User currentUser = UserServiceImpl.getUserById(user.getId());
        String oldUsername = currentUser.getUsername();
        currentUser.setUsername(username);

        LOG.info("Username of User with Id:'" + user.getId() + "' has been changed from '" + oldUsername + "' to '" + username + "'");
    }

    public void submitRatings(int[] ratings) {
        String statement = "insert into Bewertung (design, navigation, puzzle, sammelbuch) values (?, ?, ?, ?);";
        Object[] params = new Object[] {ratings[0], ratings[1], ratings[2], ratings[3]};
        jdbcTemplate.update(statement, params);

        LOG.info("A rating has been submitted {design: "+ratings[0]+", navigation: "+ratings[1]+", puzzle: " + ratings[2] + ", sammelbuch: " + ratings[3] + "}");
    }

    public String getProfilePicture(int id) {
        String statement = "SELECT profile_picture FROM Users where pk_id = ?;";
        String profilePicture = jdbcTemplate.queryForObject(statement, new Object[]{id}, (rs, rowNum) -> rs.getString("profile_picture"));

        LOG.info("DB ACCESSED to retrieve Profile Picture of user with id: '" + id + "'");

        return profilePicture;
    }

    public void changeProfilePicture(int id, String data) {
        String statement = "UPDATE Users SET profile_picture = ? WHERE pk_id = ?";
        Object[] params = new Object[] {data, id};
        jdbcTemplate.update(statement, params);

        User currentUser = UserServiceImpl.getUserById(id);

        LOG.info("Profile Picture of User: '" + currentUser.getUsername() + "' with Id:'" + id + "' has been changed");
    }
}
