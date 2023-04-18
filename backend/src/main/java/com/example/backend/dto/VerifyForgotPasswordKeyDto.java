package com.example.backend.dto;

public class VerifyForgotPasswordKeyDto {
    private String key;

    public VerifyForgotPasswordKeyDto() {
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "verifyForgotPasswordKeyDto{" +
                "key='" + key + '\'' +
                '}';
    }
}
