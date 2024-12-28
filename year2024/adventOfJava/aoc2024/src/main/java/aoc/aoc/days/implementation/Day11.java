package aoc.aoc.days.implementation;

import aoc.aoc.cache.Memoize;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static aoc.aoc.util.Utils.*;

public class Day11 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + countStonesAfterBlinksIterative(input, 25);
    }

    @Override
    protected String part2Impl(String input) {
        return "" + countStonesAfterBlinksIterative(input, 75);
    }

    private static final long MULTIPLIER = 2024L;

    private long countStonesAfterBlinksIterative(String input, int blinks) {
        Map<Long, Long> stones = streamStones(input).boxed()
                .collect(Collectors.toMap(__ -> __, __ -> 1L, Long::sum));

        for (int i = 0; i < blinks; i++) {
            Map<Long, Long> nextLevel = new HashMap<>();

            stones.forEach((stone, accumulated) -> {
                for (long s : resultingStonesAfterBlink(stone))
                    nextLevel.merge(s, accumulated, Long::sum);
            });

            stones = nextLevel;
        }

        return stones.values().stream()
                .mapToLong(Long::longValue).sum();
    }

    @SuppressWarnings("unused")
    private long countStonesAfterBlinksRecursive(String input, int blinks) {
        return streamStones(input)
                .map(stone -> processStoneRecursive(stone, blinks))
                .sum();
    }

    @Memoize
    protected long processStoneRecursive(long stone, int depth) {
        if (depth <= 0)
            return 1L;

        long accumulated = 0;
        for (var s : resultingStonesAfterBlink(stone)) {
            accumulated += processStoneRecursive(s, depth - 1);
        }

        return accumulated;
    }

    @SuppressWarnings("java:S1121")
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
