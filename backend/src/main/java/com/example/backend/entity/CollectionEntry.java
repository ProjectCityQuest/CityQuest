package com.example.backend.entity;

public class CollectionEntry {
    private int id;
    private String timestamp;
    private int locationId;
    private String location;
    private String text;
    private String image;

    public CollectionEntry(int id, String timestamp, int locationId, String location, String text, String image) {
        this.id = id;
        this.timestamp = timestamp;
        this.locationId = locationId;
        this.location = location;
        this.text = text;
        this.image = image;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "CollectionEntry{" +
                "id=" + id +
                ", timestamp='" + timestamp + '\'' +
                ", locationId=" + locationId +
                ", location='" + location + '\'' +
                ", text='" + text + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
