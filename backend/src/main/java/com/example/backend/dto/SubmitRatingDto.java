package com.example.backend.dto;

public class SubmitRatingDto {
    private final int design;
    private final int navigation;
    private final int puzzle;
    private final int sammelbuch;

    public SubmitRatingDto(int design, int navigation, int puzzle, int sammelbuch) {
        this.design = design;
        this.navigation = navigation;
        this.puzzle = puzzle;
        this.sammelbuch = sammelbuch;
    }

    public int getDesign() {
        return design;
    }

    public int getNavigation() {
        return navigation;
    }

    public int getPuzzle() {
        return puzzle;
    }

    public int getSammelbuch() {
        return sammelbuch;
    }

    @Override
    public String toString() {
        return "SubmitRatingDto{" +
                "design=" + design +
                ", navigation=" + navigation +
                ", puzzle=" + puzzle +
                ", sammelbuch=" + sammelbuch +
                '}';
    }
}
