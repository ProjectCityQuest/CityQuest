/**
 * The purpose of this class is to format the response body for a {@value /login} request
 */

package com.example.backend.dto;

import com.example.backend.entity.User;

public class UserLoginDto {
    private final int id;
    private final String name;
    private final String email;

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
