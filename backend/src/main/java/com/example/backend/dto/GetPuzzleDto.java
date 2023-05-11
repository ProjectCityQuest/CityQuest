package com.example.backend.dto;

import com.example.backend.entity.PuzzlePiece;

import java.util.List;

public class GetPuzzleDto {
    List<PuzzlePiece> pieces;

    public GetPuzzleDto(List<PuzzlePiece> pieces) {
        this.pieces = pieces;
    }

    public List<PuzzlePiece> getPieces() {
        return pieces;
    }

    public void setPieces(List<PuzzlePiece> pieces) {
        this.pieces = pieces;
    }

    @Override
    public String toString() {
        return "GetPuzzleDto{" +
                "pieces=" + pieces +
                '}';
    }
}
