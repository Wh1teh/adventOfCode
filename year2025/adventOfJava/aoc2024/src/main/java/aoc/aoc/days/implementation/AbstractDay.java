package aoc.aoc.days.implementation;

import aoc.aoc.days.enums.Part;
import aoc.aoc.days.interfaces.Day;
import aoc.aoc.days.interfaces.DaySpecifier;

public abstract class AbstractDay<T> implements Day<T>, DaySpecifier {

    private static final StringBuilder DEBUG_STRING = new StringBuilder();

    private final String dayOrdinal;

    protected Part part;
    protected boolean isSample;

    protected AbstractDay() {
        String className = this.getClass().getSimpleName();
        this.dayOrdinal = className.substring(className.length() - 2);
    }

    protected AbstractDay(int dayOrdinal) {
        this.dayOrdinal = "%02d".formatted(dayOrdinal);
    }

    @Override
    public DaySpecifier setSample(boolean isSample) {
        this.isSample = isSample;
        return this;
    }

    @Override
    public DaySpecifier setPart(Part part) {
        this.part = part;
        return this;
    }

    @Override
    public String part1(T input) {
        return part1Impl(input);
    }

    @Override
    public String part2(T input) {
        return part2Impl(input);
    }

    protected abstract String part1Impl(T input);

    protected abstract String part2Impl(T input);

    @Override
    public String debugString() {
        return DEBUG_STRING.toString();
    }

    @Override
    public void clearDebug() {
        DEBUG_STRING.setLength(0);
    }
}
