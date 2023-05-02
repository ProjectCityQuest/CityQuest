package com.example.backend.dto;

public class GetImageDto {
    private String image;

    public GetImageDto(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "GetImageDto{" +
                "image='" + image + '\'' +
                '}';
    }
}
