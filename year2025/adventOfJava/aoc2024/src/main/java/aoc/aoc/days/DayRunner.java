package aoc.aoc.days;

import aoc.aoc.benchmark.*;
import aoc.aoc.days.enums.Part;
import aoc.aoc.days.exception.DayNumberException;
import aoc.aoc.days.implementation.AbstractDay;
import aoc.aoc.days.interfaces.Day;
import aoc.aoc.days.interfaces.DayCharacterArrayParser;
import aoc.aoc.days.interfaces.DaySpecifier;
import aoc.aoc.days.interfaces.DayStringParser;
import lombok.*;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
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

        initializeBenchmarks();

        var day = createDay(dayNumber, part, isSample);
        var input = readInputFile(dayNumber, isSample, day);
        var result = runDay(part, day, input);
        
        return new DayResult(result, benchmarker.getBenchmarks());
    }

    private void initializeBenchmarks() {
        if (benchmarks) {
            benchmarker.requestNewBenchmark(DayBenchmarker.INSTANTIATION);
            benchmarker.requestNewBenchmark(DayBenchmarker.FILES);
            benchmarker.requestNewBenchmark(DayBenchmarker.IMPLEMENTATION);
        }
    }

    private Day<Object> createDay(int dayNumber, Part part, boolean isSample) {
        benchmarker.ifWasRequested(DayBenchmarker.INSTANTIATION).start();
        var day = DayFactory.createNewDay(dayNumber);
        ((DaySpecifier) day)
                .setSample(isSample)
                .setPart(part);
        benchmarker.ifWasRequested(DayBenchmarker.INSTANTIATION).end();
        return day;
    }

    private String runDay(Part part, Day<Object> day, Serializable input) {
        benchmarker.ifWasRequested(DayBenchmarker.IMPLEMENTATION).start();
        var result = part == PART_1 ? day.part1(input) : day.part2(input);
        benchmarker.ifWasRequested(DayBenchmarker.IMPLEMENTATION).end();
        return result;
    }

    private Serializable readInputFile(int dayNumber, boolean isSample, Day<?> day) throws IOException {
        benchmarker.ifWasRequested(DayBenchmarker.FILES).start();
        var dayFileName = String.format("day%02d_%d", dayNumber, isSample ? 0 : 1);
        var path = Paths.get(getDayDataPath().toString(), dayFileName);
        var input = switch (day) {
            case DayCharacterArrayParser __ -> readFileToCharArray(path);
            case DayStringParser __ -> readDayData(path);
            default -> throw new IllegalStateException("Unexpected value: " + day);
        };
        benchmarker.ifWasRequested(DayBenchmarker.FILES).end();
        return input;
    }

    private static String readDayData(Path path) throws IOException {
        return Files.readString(path);
    }

    private static char[] readFileToCharArray(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        var charBuffer = CharBuffer.allocate(bytes.length);
        var decoder = StandardCharsets.UTF_8
                .newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);
        
        decoder.decode(ByteBuffer.wrap(bytes), charBuffer, true);
        decoder.flush(charBuffer);

        int length = charBuffer.position();
        charBuffer.flip();

        char[] result = new char[length];
        charBuffer.get(result);

        return result;
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
