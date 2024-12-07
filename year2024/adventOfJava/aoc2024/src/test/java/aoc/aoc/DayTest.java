package aoc.aoc;

import aoc.aoc.days.Day;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
            "", // day07
            "",
            "", // day08
            "",
            "", // day09
            "",
            "", // day10
            "",
            "", // day11
            "",
            "", // day12
            "",
            "", // day13
            "",
            "", // day14
            "",
            "", // day15
            "",
            "", // day16
            "",
            "", // day17
            "",
            "", // day18
            "",
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

    static Stream<Object[]> provideTestData() {
        return IntStream.range(0, SAMPLE_ANSWERS.size())
                .mapToObj(index -> new Object[]{
                        index / 2 + 1, // Day
                        index % 2 + 1, // Part
                        SAMPLE_ANSWERS.get(index)
                });
    }

    @ParameterizedTest(name = "Day {0} Part {1}")
    @MethodSource("provideTestData")
    void testDays(int day, int part, String sampleAnswer) {
        assumeFalse(sampleAnswer.isBlank(), "Day not available");
        testDay(day, part, sampleAnswer);
    }

    @SneakyThrows
    private static void testDay(int dayNumber, int part, String sampleAnswer) {
        Day day = createDay(dayNumber);
        assertEquals(sampleAnswer, day.sample(part));

        String result = day.part(part);

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
}
