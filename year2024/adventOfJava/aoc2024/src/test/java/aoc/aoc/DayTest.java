package aoc.aoc;

import aoc.aoc.benchmark.Benchmarks;
import aoc.aoc.days.AbstractDay;
import aoc.aoc.days.Day;
import aoc.aoc.days.DayResult;
import aoc.aoc.benchmark.WithBenchmarks;
import aoc.aoc.testutils.DayParameter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static aoc.aoc.testutils.DayParameter.dayPart;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class DayTest {

    private static final boolean BENCHMARKS_ENABLED = true;

    @DisplayName("Read methods in abstract Day should read from classpath correctly")
    @Test
    @SneakyThrows
    void dayShouldReadFiles() {
        var resource = Objects.requireNonNull(
                DayTest.class.getClassLoader().getResource("daydata"),
                "'daydata' folder not found in classpath"
        );
        var path = Paths.get(resource.toURI()).toString();
        String sampleString = "testing sample input";
        String inputString = "testing actual input";

        Files.writeString(Paths.get(path, "day42_0"), sampleString);
        Files.writeString(Paths.get(path, "day42_1"), inputString);

        Day day = new AbstractDay(42) {

            @Override
            protected String part1Impl(String input) {
                return input;
            }

            @Override
            protected String part2Impl(String input) {
                return input;
            }
        };

        assertTrue(day.sample(1).output().contains(sampleString));
        assertTrue(day.sample(2).output().contains(sampleString));
        assertTrue(day.part(1).output().contains(inputString));
        assertTrue(day.part(2).output().contains(inputString));
    }

    private static final List<String> SAMPLE_ANSWERS = new ArrayList<>(List.of(
            "11", // day01
            "31",
            "2", // day02
            "4",
            "161", // day03
            "48",
            "18", // day04
            "9",
            "143", // day05
            "123",
            "41", // day06
            "6",
            "3749", // day07
            "11387",
            "14", // day08
            "34",
            "1928", // day09
            "2858",
            "36", // day10
            "81",
            "55312", // day11
            "",
            "1930", // day12
            "1206",
            "480", // day13
            "875318608908",
            "21", // day14
            "-1",
            "10092", // day15
            "9021",
            "7036", // day16
            "",
            "4,6,3,5,6,3,5,2,1,0", // day17
            "117440",
            "22", // day18
            "6,1",
            "6", // day19
            "16",
            "8", // day20
            "67",
            "126384", // day21
            "154115708116294",
            "37327623", // day22
            "23",
            "7", // day23
            "co,de,ka,ta",
            "2024", // day24
            "",
            "3", // day25
            ""
    ));

    public static final String SAMPLE_WORKS_INPUT_DOES_NOT = "Sample works, input does not";
    public static final String SLOW = "Solution is working but slow";
    public static final String INCOMPLETE = "Solution is incomplete";
    static final Map<DayParameter, String> DISABLED = Map.of(
            dayPart(6, 2), SAMPLE_WORKS_INPUT_DOES_NOT,
            dayPart(11, 1), SLOW,
            dayPart(11, 2), INCOMPLETE,
            dayPart(19, 2), INCOMPLETE
    );

    static Stream<Object[]> provideTestData() {
        return IntStream.range(0, SAMPLE_ANSWERS.size())
                .mapToObj(index -> new Object[]{
                        dayPart(index / 2 + 1, index % 2 + 1),
                        SAMPLE_ANSWERS.get(index)
                });
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestData")
    void testDays(DayParameter day, String sampleAnswer) {
        assumeFalse(sampleAnswer.isBlank(), "Day not available");
        var reason = DISABLED.get(day);
        assumeTrue(reason == null, "Disabled because: %s".formatted(reason));

        testDay(day.day(), day.part(), sampleAnswer);
    }

    private static void testDay(int dayNumber, int part, String sampleAnswer) {
        Day day = null;
        try {
            day = createDay(dayNumber);
        } catch (Exception e) {
            Assertions.fail("Day%02d does not exist".formatted(dayNumber), e);
        }

        try {
            assertEquals(sampleAnswer, day.sample(part).output());
            writeDebug(dayNumber, part, true, day);
        } catch (IOException e) {
            Assertions.fail("Could not read file for Day%02d part%d sample".formatted(dayNumber, part), e);
        }

        DayResult result = null;
        try {
            if (BENCHMARKS_ENABLED)
                ((WithBenchmarks) day).enableBenchmarks();

            result = day.part(part);
            writeDebug(dayNumber, part, false, day);
        } catch (IOException e) {
            Assertions.fail("Could not read file for Day%02d part%d input".formatted(dayNumber, part), e);
        }

        assertNotNull(result);
        assertNotNull(result.output());
        assertFalse(result.output().isBlank());
        if (BENCHMARKS_ENABLED)
            assertNotNull(result.benchmarks().result());

        printResults(dayNumber, part, result);
    }

    private static void printResults(int dayNumber, int part, DayResult result) {
        if (BENCHMARKS_ENABLED)
            System.out.printf("DAY %02d PART %d RESULT (%s):%n%s%n",
                    dayNumber, part, formatBenchmarkResults(result.benchmarks()), result.output());
        else
            System.out.printf("DAY %02d PART %d RESULT:%n%s%n",
                    dayNumber, part, result.output());
    }

    private static String formatBenchmarkResults(Benchmarks benchmarks) {
        return benchmarks.result().entrySet().stream()
                .map(e ->
                        "%s took %.2fms".formatted(
                                e.getKey().description(),
                                e.getValue().duration().toNanos() / 1_000_000.0
                        )
                ).collect(Collectors.joining(" | "));
    }

    private static Day createDay(int dayNumber) throws Exception {
        String className = String.format("%s.Day%02d", Day.class.getPackageName(), dayNumber);
        Class<?> dayClass = Class.forName(className);
        Object dayInstance = dayClass.getDeclaredConstructor().newInstance();

        return (Day) dayInstance;
    }

    @SneakyThrows
    private static void writeDebug(int dayNumber, int part, boolean isSample, Day day) {
        String debugString = day.debugString();
        if (debugString.isBlank())
            return;
        day.clearDebug();

        String debugDir = "target/debug";
        String fileName = debugDir + "/day%02d_part%d_%s".formatted(dayNumber, part, isSample ? "sample" : "actual");

        File file = new File(fileName);

        File parentDir = file.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            System.err.println("Failed to create directories: " + parentDir);
            return;
        }

        try (var writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(debugString);
            System.out.println("Debug file written to: " + file.getAbsolutePath());
        }
    }
}
