package aoc.aoc.days;

import aoc.aoc.benchmark.*;
import aoc.aoc.days.enums.Part;
import aoc.aoc.days.exception.DayNumberException;
import aoc.aoc.days.implementation.AbstractDay;
import aoc.aoc.days.interfaces.DaySpecifier;
import lombok.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static aoc.aoc.days.enums.Part.PART_1;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DayRunner {

    public enum RunType {
        SAMPLE,
        ACTUAL
    }

    @With(AccessLevel.PRIVATE)
    private Benchmarker benchmarker;
    @With
    private boolean benchmarks;

    public DayResult runDay(int dayNumber, Part part, RunType type) throws IOException {
        return withBenchmarker(benchmarks ? new DayBenchmarker() : new StubBenchmarker())
                .runDay(dayNumber, part, type == RunType.SAMPLE);
    }

    private DayResult runDay(int dayNumber, Part part, boolean isSample) throws IOException {
        if (dayNumber < 1 || dayNumber > 25)
            throw new DayNumberException(dayNumber);

        if (benchmarks) {
            benchmarker.requestNewBenchmark(DayBenchmarker.INSTANTIATION);
            benchmarker.requestNewBenchmark(DayBenchmarker.FILES);
            benchmarker.requestNewBenchmark(DayBenchmarker.IMPLEMENTATION);
        }

        benchmarker.ifWasRequested(DayBenchmarker.INSTANTIATION).start();
        var day = DayFactory.createNewDay(dayNumber);
        ((DaySpecifier) day)
                .setSample(isSample)
                .setPart(part);
        benchmarker.ifWasRequested(DayBenchmarker.INSTANTIATION).end();

        benchmarker.ifWasRequested(DayBenchmarker.FILES).start();
        String input = readDayData(dayNumber, isSample);
        benchmarker.ifWasRequested(DayBenchmarker.FILES).end();

        benchmarker.ifWasRequested(DayBenchmarker.IMPLEMENTATION).start();
        var result = part == PART_1 ? day.part1(input) : day.part2(input);
        benchmarker.ifWasRequested(DayBenchmarker.IMPLEMENTATION).end();

        return new DayResult(result, benchmarker.getBenchmarks());
    }

    private String readDayData(int dayNumber, boolean isSample) throws IOException {
        String dayFileName = String.format("day%02d_%d", dayNumber, isSample ? 0 : 1);
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
