package com.example.backend.dto;

import com.example.backend.entity.CollectionEntry;

public class CreateEntryDto {
    CollectionEntry entry;

    public CreateEntryDto() {}

    public CollectionEntry getEntry() {
        return entry;
    }

    public void setEntry(CollectionEntry entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return "CreateEntryDto{" +
                "entry=" + entry +
                '}';
    }
}
