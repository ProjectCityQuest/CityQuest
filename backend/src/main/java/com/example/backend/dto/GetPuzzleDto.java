package com.example.backend.dto;

import com.example.backend.entity.PuzzlePiece;

import java.util.List;

public class GetPuzzleDto {
    int pageIndex;
    List<PuzzlePiece> pieces;

    public GetPuzzleDto(List<PuzzlePiece> pieces, int pageIndex) {
        this.pieces = pieces;
        this.pageIndex = pageIndex;
    }

    public List<PuzzlePiece> getPieces() {
        return pieces;
    }

    public void setPieces(List<PuzzlePiece> pieces) {
        this.pieces = pieces;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public String toString() {
        return "GetPuzzleDto{" +
                "pageIndex=" + pageIndex +
                ", pieces=" + pieces +
                '}';
    }
}
