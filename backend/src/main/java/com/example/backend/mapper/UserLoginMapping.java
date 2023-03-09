package com.example.backend.mapper;

import com.example.backend.dto.UserLoginDto;

public class UserLoginMapping {
    private UserLoginDto user;

    public UserLoginDto getUser() {
        return user;
    }

    public UserLoginMapping(UserLoginDto user) {
        this.user = user;
    }
}
