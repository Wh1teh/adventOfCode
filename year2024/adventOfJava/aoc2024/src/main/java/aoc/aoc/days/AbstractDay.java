package aoc.aoc.days;

public abstract class AbstractDay implements Day, DaySpecifier {

    private static final StringBuilder DEBUG_STRING = new StringBuilder();

    private final String dayOrdinal;

    protected Part part;
    protected boolean isSample;

    AbstractDay() {
        String className = this.getClass().getSimpleName();
        this.dayOrdinal = className.substring(className.length() - 2);
    }

    protected AbstractDay(int part) {
        this.dayOrdinal = "%02d".formatted(part);
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
    public String part1(String input) {
        return part1Impl(input);
    }

    @Override
    public String part2(String input) {
        return part2Impl(input);
    }

    protected abstract String part1Impl(String input);

    protected abstract String part2Impl(String input);

    @Override
    public String debugString() {
        return DEBUG_STRING.toString();
    }

    @Override
    public void clearDebug() {
        DEBUG_STRING.setLength(0);
    }
}
