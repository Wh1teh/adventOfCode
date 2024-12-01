package aoc.aoc;

import aoc.aoc.days.Day;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void day01_part1() {
        testDay(1,1, "11");
    }

    @Test
    void day01_part2() {
        testDay(1,2, "31");
    }

    @SneakyThrows
    private static void testDay(int dayNumber, int part, String sampleAnswer) {
        Day day = createDay(dayNumber);
        assertEquals(sampleAnswer, day.sample(part));

        String result = day.part(part);

        assertNotNull(result);
        assertFalse(result.isBlank());

        System.out.println(result);
    }

    private static Day createDay(int dayNumber) throws Exception {
        String className = String.format("%s.Day%02d", Day.class.getPackageName(), dayNumber);
        Class<?> dayClass = Class.forName(className);
        Object dayInstance = dayClass.getDeclaredConstructor().newInstance();

        return (Day) dayInstance;
    }
}
