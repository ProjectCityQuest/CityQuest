package com.example.backend.dto;

public class GetEntryRequestDto {
    private int id;

    public GetEntryRequestDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GetEntryRequestDto{" +
                "id=" + id +
                '}';
    }
}
