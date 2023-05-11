package com.example.backend.service;

import com.example.backend.db.DatabaseAccess;
import com.example.backend.db.DatabaseAccessImplementation;
import com.example.backend.entity.PuzzlePiece;

import java.util.List;

public class PuzzleService {
    private static final DatabaseAccess DATABASE = new DatabaseAccessImplementation();

    /**
     *
     * @param id of the user
     */

    public static List<PuzzlePiece> getPuzzle(int id) {
        return DATABASE.getPuzzle(id);
    }
}
