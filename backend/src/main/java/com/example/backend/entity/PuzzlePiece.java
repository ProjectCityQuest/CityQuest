package com.example.backend.entity;

public class PuzzlePiece {
    int id;
    String location_name;
    String image;
    int column;
    int row;

    public PuzzlePiece(int id, String location_name, String image, int column, int row) {
        this.id = id;
        this.location_name = location_name;
        this.image = image;
        this.column = column;
        this.row = row;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public String toString() {
        return "PuzzlePiece{" +
                "id=" + id +
                ", location_name='" + location_name + '\'' +
                ", image='" + image + '\'' +
                ", column=" + column +
                ", row=" + row +
                '}';
    }
}
