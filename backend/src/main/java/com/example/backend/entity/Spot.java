package com.example.backend.entity;

import java.util.Arrays;

public class Spot {
    int id;
    String name;
    double[] coords;
    String description;

    public Spot(int id, String name, double[] coords, String description) {
        this.id = id;
        this.name = name;
        this.coords = coords;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getCoords() {
        return coords;
    }

    public void setCoords(double[] coords) {
        this.coords = coords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Spot{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coords=" + Arrays.toString(coords) +
                ", description='" + description + '\'' +
                '}';
    }
}