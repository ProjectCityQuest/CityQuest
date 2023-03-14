package com.example.backend.mapper;

import com.example.backend.dto.UserRegisterDto;
import com.example.backend.entity.User;

public class UserRegisterMapping {
    private UserRegisterDto user;
    private boolean emailVerified;

    @Override
    public String toString() {
        return "UserRegisterMapping{" +
                "user=" + user +
                ", emailVerified=" + emailVerified +
                '}';
    }

    public UserRegisterMapping(UserRegisterDto user, boolean emailVerified) {
        this.user = user;
        this.emailVerified = emailVerified;
    }

    public UserRegisterDto getUser() {
        return user;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }
}
