package aoc.aoc;

import aoc.aoc.days.Day;
import aoc.aoc.testutils.DayParameter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static aoc.aoc.testutils.DayParameter.dayPart;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class DayTest {

    @DisplayName("Read methods in abstract Day should read from classpath correctly")
    @Test
    void dayShouldReadFiles() {
        Day day = new Day42();
        assertTrue(day.sample(1).contains("testing"));
        assertTrue(day.sample(2).contains("testing"));
        assertTrue(day.part(1).contains("another test"));
        assertTrue(day.part(2).contains("another test"));
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
            "", // day19
            "",
            "", // day20
            "",
            "", // day21
            "",
            "", // day22
            "",
            "", // day23
            "",
            "", // day24
            "",
            "", // day25
            ""
    ));

    public static final String SAMPLE_WORKS_INPUT_DOES_NOT = "Sample works, input does not";
    public static final String SLOW = "Solution is working but slow";
    public static final String INCOMPLETE = "Solution is incomplete";
    static final Map<DayParameter, String> DISABLED = Map.of(
            dayPart(6, 2), SAMPLE_WORKS_INPUT_DOES_NOT,
            dayPart(11, 1), SLOW,
            dayPart(11, 2), INCOMPLETE,
            dayPart(17, 2), INCOMPLETE
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

    @SneakyThrows
    private static void testDay(int dayNumber, int part, String sampleAnswer) {
        Day day = createDay(dayNumber);

        assertEquals(sampleAnswer, day.sample(part));
        writeDebug(dayNumber, part, true, day);

        String result = day.part(part);
        writeDebug(dayNumber, part, false, day);

        assertNotNull(result);
        assertFalse(result.isBlank());

        System.out.printf("DAY %d PART %d RESULT:%n%s%n", dayNumber, part, result);
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
