package com.example.backend.service;

import com.example.backend.db.DatabaseAccess;
import com.example.backend.db.DatabaseAccessImplementation;

public class RatingsService {
    private static final DatabaseAccess DATABASE = new DatabaseAccessImplementation();

    /**
     *
     * @param ratings contains the different ratings ranging from 1 to 5
     *                0: design
     *                1: navigation
     *                2: puzzle
     *                3: sammelbuch
     */


    public static void submitRatings(int[] ratings) {
        DATABASE.submitRatings(ratings);
    }
}
