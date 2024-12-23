package aoc.aoc.days;

import aoc.aoc.benchmark.DayBenchmarker;
import aoc.aoc.benchmark.WithBenchmarks;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.StandardException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static aoc.aoc.days.Part.PART_1;

public abstract class AbstractDay implements Day, WithBenchmarks {

    private static final StringBuilder DEBUG_STRING = new StringBuilder();
    @Getter
    private final DayBenchmarker benchmarker = new DayBenchmarker();

    private boolean benchmarksEnabled = false;
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

    protected abstract String part1Impl(String input);

    protected abstract String part2Impl(String input);

    @Override
    public void enableBenchmarks() {
        benchmarksEnabled = true;
    }

    @Override
    public DayResult sample(int part) {
        return runDay(part, true);
    }

    @Override
    public DayResult part(int part) {
        return runDay(part, false);
    }

    private DayResult runDay(int part, boolean shouldRunSample) {
        if (part != 1 && part != 2)
            throw new DayPartException(part);

        var day = createNewInstance();
        day.setSample(shouldRunSample);
        day.setPart(Part.of(part));

        if (benchmarksEnabled) {
            day.enableBenchmarks();
            day.benchmarker.requestNewBenchmark(DayBenchmarker.FILES);
            day.benchmarker.requestNewBenchmark(DayBenchmarker.IMPLEMENTATION);
        }

        day.benchmarker.ifWasRequested(DayBenchmarker.FILES).start();
        String input = read(shouldRunSample ? 0 : 1);
        day.benchmarker.ifWasRequested(DayBenchmarker.FILES).end();

        day.benchmarker.ifWasRequested(DayBenchmarker.IMPLEMENTATION).start();
        var result = day.part == PART_1 ? day.part1Impl(input) : day.part2Impl(input);
        day.benchmarker.ifWasRequested(DayBenchmarker.IMPLEMENTATION).end();

        return new DayResult(result, day.benchmarker.getBenchmarks());
    }

    @SneakyThrows
    private AbstractDay createNewInstance() {
        return (AbstractDay) this.clone();
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

    private static class DayPartException extends DayException {

        public DayPartException(int part) {
            super("Part must be 1 or 2. Was: %d".formatted(part));
        }
    }

    @StandardException
    private static class DayException extends RuntimeException {
    }
}
