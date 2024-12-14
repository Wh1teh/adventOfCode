package aoc.aoc.days;

import lombok.Setter;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public abstract class AbstractDay implements Day {

    private static final StringBuilder DEBUG_STRING = new StringBuilder();
    private final String dayOrdinal;

    @Setter
    protected Part part;

    protected AbstractDay() {
        String className = this.getClass().getSimpleName();
        this.dayOrdinal = className.substring(className.length() - 2);
    }

    @SuppressWarnings("java:S112")
    public String sample(int part) {
        if (part != 1 && part != 2) {
            throw new RuntimeException("Part must be 1 or 2");
        }

        return part == 1 ? sample1() : sample2();
    }

    @SuppressWarnings("java:S112")
    public String part(int part) {
        if (part != 1 && part != 2) {
            throw new RuntimeException("Part must be 1 or 2");
        }

        return part == 1 ? part1() : part2();
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
