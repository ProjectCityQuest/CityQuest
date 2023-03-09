package com.example.backend.dto;

import com.example.backend.entity.User;

public class UserLoginDto {
    private int id;
    private String name;
    private String email;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserLoginDto(User user) {
        this.id = user.getId();
        this.name = user.getUsername();
        this.email = user.getEmail();
    }
}
