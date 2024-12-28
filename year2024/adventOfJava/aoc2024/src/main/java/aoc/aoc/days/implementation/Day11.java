package aoc.aoc.days.implementation;

import aoc.aoc.cache.Memoize;

import java.util.*;
import java.util.stream.LongStream;

import static aoc.aoc.util.Utils.arrayOf;
import static aoc.aoc.util.Utils.isEven;

public class Day11 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + countStonesAfterBlinks(input, 25);
    }

    @Override
    protected String part2Impl(String input) {
        return "" + countStonesAfterBlinks(input, 75);
    }

    private static final long MULTIPLIER = 2024L;

    private long countStonesAfterBlinks(String input, int blinks) {
        return streamStones(input)
                .map(stone -> processStone(stone, blinks))
                .sum();
    }

    @Memoize
    protected long processStone(long stone, int depth) {
        if (depth <= 0)
            return 1L;

        long accumulated = 0;
        for (var s : resultingStonesAfterBlink(stone)) {
            accumulated += processStone(s, depth - 1);
        }

        return accumulated;
    }

    @SuppressWarnings("java:S1121")
    @Memoize
    protected long[] resultingStonesAfterBlink(long stone) {
        int stoneLength;
        if (stone == 0)
            return arrayOf(1L);
        else if (isEven(stoneLength = numberLength(stone)))
            return splitNumber(stone, stoneLength);
        else
            return arrayOf(stone * MULTIPLIER);
    }

    private static int numberLength(long n) {
        return (int) (Math.log10(n) + 1);
    }

    private static long[] splitNumber(long n, long length) {
        long mid = (long) Math.pow(10, length / 2.0);
        long right = n % mid;
        long left = n / mid;
        return arrayOf(left, right);
    }

    private LongStream streamStones(String input) {
        return Arrays.stream(input.trim().split(" "))
                .mapToLong(Long::parseLong);
    }
}
