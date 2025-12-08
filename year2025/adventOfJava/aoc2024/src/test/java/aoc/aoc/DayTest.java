package aoc.aoc;

import aoc.aoc.benchmark.Benchmarks;
import aoc.aoc.days.*;
import aoc.aoc.days.enums.Part;
import aoc.aoc.days.interfaces.Day;
import aoc.aoc.testutils.DayParameter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static aoc.aoc.testutils.DayParameter.dayPart;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class DayTest {

    private static final boolean BENCHMARKS_ENABLED = true;

    private static final List<String> SAMPLE_ANSWERS = new ArrayList<>(List.of(
            "3", // day01
            "6",
            "1227775554", // day02
            "4174379265",
            "357", // day03
            "3121910778619",
            "13", // day04
            "43",
            "3", // day05
            "14",
            "4277556", // day06
            "3263827",
            "21", // day07
            "40",
            "40", // day08
            "25272"
    ));

    @SuppressWarnings("unused")
    public static final String SAMPLE_WORKS_INPUT_DOES_NOT = "Sample works, input does not";
    @SuppressWarnings("unused")
    public static final String SLOW = "Solution is working but slow";
    @SuppressWarnings("unused")
    public static final String INCOMPLETE = "Solution is incomplete";
    static final Map<DayParameter, String> DISABLED = Map.of(
//            dayPart(19, 2), INCOMPLETE
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

    private static void testDay(int dayNumber, int part, String sampleExpected) {
        var runner = new DayRunner().withBenchmarks(BENCHMARKS_ENABLED);

        try {
            var sampleActual = runner.runDay(dayNumber, Part.of(part), DayRunner.RunType.SAMPLE).output();
            assertEquals(sampleExpected, sampleActual);
        } catch (IOException e) {
            Assertions.fail("Could not read file for Day%02d part%d sample".formatted(dayNumber, part), e);
        }

        DayResult result = null;
        try {
            result = runner.runDay(dayNumber, Part.of(part), DayRunner.RunType.ACTUAL);
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

    @SuppressWarnings("unused")
    @SneakyThrows
    private static void writeDebug(int dayNumber, int part, boolean isSample, Day<?> day) {
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
