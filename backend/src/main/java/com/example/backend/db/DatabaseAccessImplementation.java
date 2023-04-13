/**
 * This class implements the basic {@link com.example.backend.db.DatabaseAccess} interface
 * and uses a MySQL database to save the data persistently
 */

package com.example.backend.db;

import com.example.backend.entity.User;
import com.example.backend.service.UserServiceImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.*;

public class DatabaseAccessImplementation implements DatabaseAccess {
    /**
     * DB_URL is the url of the database
     * DB_USERNAME is the database users username
     * DB_PASSWORD is the database users password
     */

    private final String DB_URL = "jdbc:mysql://localhost:3306/cityquest";
    private final String DB_USERNAME = "root";
    private final String DB_PASSWORD = "";

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
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
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
        List<Map<String, Object>> users = jdbcTemplate.queryForList("SELECT * FROM User");

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
    }

    /**
     * @see DatabaseAccess
     */
    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    /**
     * @see DatabaseAccess
     */
    public void createUser(User user) {
        String statement = "insert into User (username, password, email) values ('" + user.getUsername() + "', '" + user.getPassword() + "', '" + user.getEmail() + "');";
        jdbcTemplate.execute(statement);

        List<Map<String, Object>> users = jdbcTemplate.queryForList("SELECT * FROM User where username = '"+user.getUsername()+"';");

        Map<String, Object> currentUser = users.get(0);

        int id = Integer.parseInt(currentUser.get("pk_id")+"");
        String username = currentUser.get("username")+"";
        String password = currentUser.get("password")+"";
        String email = currentUser.get("email")+"";
        User newUser = new User(username, email, password);
        newUser.setId(id);

        userList.add(newUser);
    }

    /**
     *
     * @see DatabaseAccess
     */

    public void deleteUser(User user) {
        String statement = "DELETE from User where pk_id = " + user.getId();
        jdbcTemplate.execute(statement);

        userList.remove(user);
    }

    public void changePassword(User user, String password) {
        String statement = "UPDATE User SET password = '" + password +"' WHERE pk_id = " + user.getId();
        jdbcTemplate.execute(statement);

        UserServiceImpl.getUserById(user.getId()).setPassword(password);
    }
}
