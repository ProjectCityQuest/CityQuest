/**
 * The purpose of this class is to format the response body for an authorization
 */

package com.example.backend.dto;

public class AuthorizationDto {
    private final String sessionKey;

    private final boolean firstLogin;

    public String getSessionKey() {
        return sessionKey;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public AuthorizationDto(String sessionKey, boolean firstLogin) {
        this.sessionKey = sessionKey;
        this.firstLogin = firstLogin;
    }
}
