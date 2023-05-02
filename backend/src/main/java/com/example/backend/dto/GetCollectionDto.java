package com.example.backend.dto;

import com.example.backend.entity.CollectionEntry;

import java.util.List;

public class GetCollectionDto {
    private List<CollectionEntry> entries;

    public GetCollectionDto(List<CollectionEntry> entries) {
        this.entries = entries;
    }

    public List<CollectionEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<CollectionEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "GetCollectionDto{" +
                "entries=" + entries +
                '}';
    }
}
