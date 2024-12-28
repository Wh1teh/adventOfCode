package aoc.aoc.days.implementation;

import aoc.aoc.cache.Memoize;

import java.math.BigInteger;
import java.util.*;

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

    private static final BigInteger MULTIPLIER = BigInteger.valueOf(2024L);

    private long countStonesAfterBlinks(String input, int blinks) {
        long totalStones = 0L;

        for (var stone : parseStones(input)) {
            var result = processStone(stone, blinks);
            totalStones += result;
        }

        return totalStones;
    }

    @Memoize
    protected long processStone(String stone, int depth) {
        if (depth <= 0)
            return 1L;

        long accumulated = 0;
        for (var s : resultingInStonesAfterBlink(stone)) {
            accumulated += processStone(s, depth - 1);
        }

        return accumulated;
    }

    protected List<String> resultingInStonesAfterBlink(String stone) {
        if (stone.equals("0")) {
            return Collections.singletonList("1");
        } else if (isEven(stone.length())) {
            var midPoint = stone.length() / 2;
            var firstHalf = stone.substring(0, midPoint);
            var secondHalf = parseNumbersFromRight(stone.substring(midPoint));
            return List.of(firstHalf, secondHalf);
        } else {
            return Collections.singletonList(new BigInteger(stone).multiply(MULTIPLIER).toString());
        }
    }

    private String parseNumbersFromRight(String number) {
        var sb = new StringBuilder();

        boolean leadingZeroes = true;
        for (int i = 0; i < number.length(); i++) {
            if (leadingZeroes && number.charAt(i) != '0')
                leadingZeroes = false;
            if (leadingZeroes && number.charAt(i) == '0')
                continue;
            sb.append(number.charAt(i));
        }

        return sb.isEmpty() ? "0" : sb.toString();
    }

    private List<String> parseStones(String input) {
        return new ArrayList<>(Arrays.asList(input.trim().split(" ")));
    }
}
