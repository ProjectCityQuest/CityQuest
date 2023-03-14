package com.example.backend.entity;

import com.example.backend.service.UserServiceImpl;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String token;
    private boolean emailIsVerified = false;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", emailIsVerified=" + emailIsVerified +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEmailIsVerified() {
        return emailIsVerified;
    }

    public void setEmailIsVerified(boolean emailIsVerified) {
        this.emailIsVerified = emailIsVerified;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.id = UserServiceImpl.getUserList().size();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

}
