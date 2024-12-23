package aoc.aoc.days;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class AbstractDay implements Day {

    private static final StringBuilder DEBUG_STRING = new StringBuilder();
    private final String dayOrdinal;

    @Setter(AccessLevel.PRIVATE)
    protected Part part;

    @Setter(AccessLevel.PRIVATE)
    protected boolean isSample;

    AbstractDay() {
        String className = this.getClass().getSimpleName();
        this.dayOrdinal = className.substring(className.length() - 2);
    }

    protected AbstractDay(int part) {
        this.dayOrdinal = "%02d".formatted(part);
    }

    @SuppressWarnings("java:S112")
    public String sample(int part) {
        if (part != 1 && part != 2)
            throw new RuntimeException("Part must be 1 or 2");

        var day = createNewInstance();
        return runPart(day, true, part, day::sample1, day::sample2);
    }

    @SuppressWarnings("java:S112")
    public String part(int part) {
        if (part != 1 && part != 2)
            throw new RuntimeException("Part must be 1 or 2");

        var day = createNewInstance();
        return day.runPart(day, false, part, day::part1, day::part2);
    }

    @SneakyThrows
    private AbstractDay createNewInstance() {
        return (AbstractDay) this.clone();
    }

    private String runPart(
            AbstractDay day, boolean isSample, int part, Supplier<String> part1, Supplier<String> part2
    ) {
        day.setSample(isSample);
        day.setPart(Part.values()[part - 1]);
        return part == 1 ? part1.get() : part2.get();
    }

    @Override
    public String debugString() {
        return DEBUG_STRING.toString();
    }

    @Override
    public void clearDebug() {
        DEBUG_STRING.setLength(0);
    }

    protected static void appendDebug(String str) {
        DEBUG_STRING.append(str);
    }

    protected String sample1() {
        return part1Impl(readSample());
    }

    protected String sample2() {
        return part2Impl(readSample());
    }

    protected String part1() {
        return part1Impl(read());
    }

    protected String part2() {
        return part2Impl(read());
    }

    protected abstract String part1Impl(String input);

    protected abstract String part2Impl(String input);

    protected String readSample() {
        return read(0);
    }

    protected String read() {
        return read(1);
    }

    @SneakyThrows
    private String read(int part) {
        String dayFileName = String.format("day%s_%d", dayOrdinal, part);
        Path path = Paths.get(getDayDataPath().toString(), dayFileName);
        return Files.readString(path);
    }

    @SneakyThrows
    private static Path getDayDataPath() {
        var resource = Objects.requireNonNull(
                AbstractDay.class.getClassLoader().getResource("daydata"),
                "'daydata' folder not found in classpath"
        );

        return Paths.get(resource.toURI());
    }
}
