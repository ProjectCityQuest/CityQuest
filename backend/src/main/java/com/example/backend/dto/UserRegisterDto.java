/**
 * The purpose of this class is to format the response body for a {@value /register} request
 */

package com.example.backend.dto;

import com.example.backend.entity.User;

public class UserRegisterDto {
    private final int id;
    private String username;
    private String email;
    private String password;

    public UserRegisterDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
