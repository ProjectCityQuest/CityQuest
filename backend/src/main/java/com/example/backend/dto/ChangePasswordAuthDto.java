package com.example.backend.dto;

public class ChangePasswordAuthDto {
    private final String old_password;
    private final String new_password;

    public ChangePasswordAuthDto(String old_password, String new_password) {
        this.old_password = old_password;
        this.new_password = new_password;
    }

    public String getOldPassword() {
        return old_password;
    }

    public String getNewPassword() {
        return new_password;
    }

    @Override
    public String toString() {
        return "ChangePasswordAuthDto{" +
                "oldPassword='" + old_password + '\'' +
                ", newPassword='" + new_password + '\'' +
                '}';
    }
}
