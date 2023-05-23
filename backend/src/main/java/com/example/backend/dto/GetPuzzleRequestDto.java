package com.example.backend.dto;

public class GetPuzzleRequestDto {
    int pageIndex;

    public GetPuzzleRequestDto() {
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public String toString() {
        return "GetPuzzleRequestDto{" +
                "pageIndex=" + pageIndex +
                '}';
    }
}
