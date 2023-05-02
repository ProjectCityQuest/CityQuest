package com.example.backend.dto;

public class ChangeImageDto {
    private String image;

    public ChangeImageDto() {}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ChangeImageDto{" +
                "image='" + image + '\'' +
                '}';
    }
}
