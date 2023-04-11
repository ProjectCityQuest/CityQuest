package com.example.backend.dto;

public class ChangePasswordDto {
    private String key;
    private String email;
    private String new_password;

    public ChangePasswordDto(String key, String email, String new_password) {
        this.key = key;
        this.email = email;
        this.new_password = new_password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    @Override
    public String toString() {
        return "ChangePasswordDto{" +
                "key='" + key + '\'' +
                ", email='" + email + '\'' +
                ", new_password='" + new_password + '\'' +
                '}';
    }
}
