package com.example.backend.dto;

public class AuthorizationDto {
    private final String sessionKey;

    public String getSessionKey() {
        return sessionKey;
    }

    public AuthorizationDto(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
