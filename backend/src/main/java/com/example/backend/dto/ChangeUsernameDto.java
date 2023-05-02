package com.example.backend.dto;

public class ChangeUsernameDto {
    private String username;

    public ChangeUsernameDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ChangeUsernameDto{" +
                "username='" + username + '\'' +
                '}';
    }
}
