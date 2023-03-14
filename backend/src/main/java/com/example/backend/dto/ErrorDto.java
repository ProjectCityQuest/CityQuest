package com.example.backend.dto;

public class ErrorDto {
    private final String error;

    public String getError() {
        return error;
    }

    public ErrorDto(String error) {
        this.error = error;
    }
}
