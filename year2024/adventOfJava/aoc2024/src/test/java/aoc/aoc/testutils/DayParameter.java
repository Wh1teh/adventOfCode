package aoc.aoc.testutils;

public record DayParameter(int day, int part) {

    public static DayParameter dayPart(int day, int part) {
        return new DayParameter(day, part);
    }

    public String toString() {
        return "Day %02d - Part %d".formatted(day, part);
    }
}
