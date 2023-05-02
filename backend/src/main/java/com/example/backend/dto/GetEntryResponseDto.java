package com.example.backend.dto;

import com.example.backend.entity.CollectionEntry;

public class GetEntryResponseDto {
    CollectionEntry entry;

    public GetEntryResponseDto(CollectionEntry entry) {
        this.entry = entry;
    }

    public CollectionEntry getEntry() {
        return entry;
    }

    public void setEntry(CollectionEntry entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return "GetEntryResponseDto{" +
                "entry=" + entry +
                '}';
    }
}
