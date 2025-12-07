package aoc.aoc.days.implementation;

import aoc.aoc.days.enums.Part;
import aoc.aoc.days.interfaces.DayStringParser;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static aoc.aoc.days.enums.Part.PART_1;
import static aoc.aoc.days.enums.Part.PART_2;

public class Day02 extends DayStringParser {

    @Override
    protected Long part1Impl(String input) {
        return normalizeInput(input)
                .mapToLong(range -> findInvalidIds(range, PART_1))
                .sum();
    }

    @Override
    protected Long part2Impl(String input) {
        return normalizeInput(input)
                .mapToLong(range -> findInvalidIds(range, PART_2))
                .sum();
    }

    private static Stream<String> normalizeInput(String input) {
        return Arrays.stream(input.lines().collect(Collectors.joining()).split(","));
    }

    private static long findInvalidIds(String range, Part part) {
        var split = range.split("-");
        final long end = Long.parseLong(split[1]);
        long currentId = Long.parseLong(split[0]);

        long accumulate = 0;
        while (currentId <= end) {
            if (isInvalid(currentId, part))
                accumulate += currentId;
            ++currentId;
        }

        return accumulate;
    }

    private static boolean isInvalid(long id, Part part) {
        int length = calculateDigits(id);
        for (int i = 2; i <= (part == PART_1 ? 2 : length); i++) {
            if (setFromEqualPartsNew(id, i, length))
                return true;
        }

        return false;
    }

    private static int calculateDigits(long id) {
        return (id == 0) ? 1 : (int) Math.log10(id) + 1;
    }

    private static boolean setFromEqualPartsNew(long number, final int n, int length) {
        boolean notDivisible = length % n != 0;
        if (notDivisible)
            return false;

        int bufferSize = length / n;
        long divisor = calculateDivisor(bufferSize);
        long expected = number % divisor;
        number /= divisor;
        for (int i = 1; i < n; i++) {
            if (number % divisor != expected)
                return false;

            number /= divisor;
        }

        return true;
    }

    private static long calculateDivisor(int bufferSize) {
        long divisor = 1;
        for (int i = 0; i < bufferSize; i++)
            divisor *= 10;
        return divisor;
    }
}
