/**
 * The purpose of this class is to format the response body for a verification
 */

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

    @Override
    public String toString() {
        return "UserVerifyDto{" +
                "key='" + key + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
