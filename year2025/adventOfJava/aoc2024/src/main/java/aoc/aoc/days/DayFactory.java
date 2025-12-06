package aoc.aoc.days;

import aoc.aoc.cache.MemoizeApplier;
import aoc.aoc.days.exception.DayCreationException;
import aoc.aoc.days.implementation.AbstractDay;
import aoc.aoc.days.interfaces.Day;
import lombok.SneakyThrows;

public class DayFactory {

    private DayFactory() {
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public static Day<Object> createNewDay(int of) {
        var day = createDay(of);
        day = MemoizeApplier.recreateWithMemoizeApplied(day);
        return (Day<Object>) day;
    }

    private static Day<?> createDay(int dayNumber) {
        try {
            String className = String.format("%s.Day%02d", AbstractDay.class.getPackageName(), dayNumber);
            Class<?> dayClass = Class.forName(className);
            Object dayInstance = dayClass.getDeclaredConstructor().newInstance();

            return (Day<?>) dayInstance;
        } catch (Exception e) {
            throw new DayCreationException(e);
        }
    }
}
