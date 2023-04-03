/**
 * This class is just a formatter to return the json body of a {@value /register} request in the correct format
 */

package com.example.backend.mapper;

import com.example.backend.dto.UserRegisterDto;

public class UserRegisterMapping {
    private final UserRegisterDto user;
    private final boolean emailVerified;

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
