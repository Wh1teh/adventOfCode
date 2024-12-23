package aoc.aoc.days;

public enum Part {
    PART_1,
    PART_2;

    public static Part of(int part) {
        return values()[part - 1];
    }
}
