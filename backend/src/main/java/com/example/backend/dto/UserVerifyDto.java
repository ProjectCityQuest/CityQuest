package com.example.backend.dto;

public class UserVerifyDto {
    String key;
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserVerifyDto() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "UserVerifyDto{" +
                "key='" + key + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
