package com.example.backend.service;

import com.example.backend.db.DatabaseAccess;
import com.example.backend.db.DatabaseAccessImplementation;
import com.example.backend.entity.Spot;

import java.util.List;

public class SpotService {
    private static final DatabaseAccess DATABASE = new DatabaseAccessImplementation();

    public static List<Spot> getSpots(int userId) {
        return DATABASE.getSpots(userId);
    }
}
