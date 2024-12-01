package aoc.aoc;

import aoc.aoc.days.Day;
import aoc.aoc.days.Day01;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DayTest {

    @DisplayName("Read methods in abstract Day should read from classpath correctly")
    @Test
    void dayShouldReadFiles() {
        Day day = new Day42();
        assertTrue(day.part0().contains("testing"));
        assertTrue(day.part1().contains("another test"));
        assertTrue(day.part2().contains("and another"));
    }

    @Test
    void day01_part0() {
        Day day = new Day01();

    }
}
