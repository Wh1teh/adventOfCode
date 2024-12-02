package aoc.aoc.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day02 extends AbstractDay {

    public static final int LOWER_BOUNDARY = 1;
    public static final int UPPER_BOUNDARY = 3;

    @Override
    protected String part1Impl(String input) {
        AtomicInteger safeReports = new AtomicInteger();
        input.lines().forEach(line -> safeReports.addAndGet(isSafePart1(line) ? 1 : 0));

        return "" + safeReports;
    }

    @Override
    protected String part2Impl(String input) {
        AtomicInteger safeReports = new AtomicInteger();
        input.lines().forEach(line -> safeReports.addAndGet(isSafePart2(line) ? 1 : 0));

        return "" + safeReports;
    }

    private boolean isSafePart1(String line) {
        var numbers = Arrays.stream(line.split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toCollection(ArrayList::new));

        return isSafe(numbers);
    }

    private static boolean isSafe(List<Integer> numbers) {
        boolean isAscending = numbers.get(0) < numbers.get(1);

        for (int i = 0; i + 1 < numbers.size(); i++) {
            int current = numbers.get(i);
            int next = numbers.get(i + 1);

            int distance = Math.abs(current - next);
            if (!isDistanceKept(distance) || !isDirectionKept(isAscending, current, next)) {
                return false;
            }
        }

        return true;
    }

    private boolean isSafePart2(String line) {
        var numbers = Arrays.stream(line.split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toCollection(ArrayList::new));

        if (isSafe(numbers)) {
            return true;
        }

        for (int i = 0; i < numbers.size(); i++) {
            int index = i;
            var modifiedList = IntStream.range(0, numbers.size())
                    .filter(__ -> __ != index)
                    .mapToObj(numbers::get)
                    .toList();

            if (isSafe(modifiedList)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isDistanceKept(int distance) {
        return distance >= Day02.LOWER_BOUNDARY && distance <= Day02.UPPER_BOUNDARY;
    }

    private static boolean isDirectionKept(boolean isAscending, int current, int next) {
        return isAscending ? current <= next : current >= next;
    }
}
