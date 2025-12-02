package aoc.aoc.days.implementation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day02 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + normalizeInput(input)
                .mapToLong(range -> {
                    var split = range.split("-");
                    final long end = Long.parseLong(split[1]);
                    long current = Long.parseLong(split[0]);

                    long accumulate = 0;
                    while (current <= end) {
                        if (setFromEqualParts("" + current, 2).size() == 1)
                            accumulate += current;
                        ++current;
                    }

                    return accumulate;
                })
                .sum();
    }

    @Override
    protected String part2Impl(String input) {
        return "" + normalizeInput(input)
                .mapToLong(range -> {
                    var split = range.split("-");
                    final long end = Long.parseLong(split[1]);
                    long current = Long.parseLong(split[0]);

                    long accumulate = 0;
                    while (current <= end) {
                        if (isInvalidPart2("" + current))
                            accumulate += current;
                        ++current;
                    }

                    return accumulate;
                })
                .sum();
    }

    private static Stream<String> normalizeInput(String input) {
        return Arrays.stream(input.lines().collect(Collectors.joining()).split(","));
    }

    private static boolean isInvalidPart2(String id) {
        for (int i = 2; i <= id.length(); i++) {
            if (setFromEqualParts(id, i).size() == 1)
                return true;
        }

        return false;
    }

    private static Set<String> setFromEqualParts(String str, final int n) {
        if (str.length() % n != 0)
            return Collections.emptySet();

        Set<String> parts = new HashSet<>();
        final int bufferSize = str.length() / n;
        for (int i = 0; i < n; i++) {
            parts.add(
                    str.substring(i * bufferSize, (i + 1) * bufferSize)
            );
        }

        return parts;
    }
}
