package aoc.aoc.days.exception;

public class DayNumberException extends DayException {

    public DayNumberException(int day) {
        super("Day must be 1-25. Was: %d".formatted(day));
    }
}
