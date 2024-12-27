package aoc.aoc.days.implementation;

import aoc.aoc.solver.AbstractSolver;

import java.math.BigInteger;
import java.util.*;

import static aoc.aoc.util.Utils.isEven;

public class Day11 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + new Blinker(input)
                .countStonesWith(25);
    }

    @Override
    protected String part2Impl(String input) {
        return "" + new Blinker(input)
                .countStonesWith(75);
    }


    private static class Blinker extends AbstractSolver<Blinker> {

        private static final BigInteger MULTIPLIER = BigInteger.valueOf(2024L);

        private final List<String> stones;
        private final Map<String, List<List<String>>> knownAnswers = new HashMap<>();

        public Blinker(String input) {
            this.stones = new ArrayList<>(Arrays.asList(input.trim().split(" ")));
        }

        public int countStonesWith(int blinks) {
            for (int i = 0; i < blinks; i++) {
                blink();
            }

            return stones.size();
        }

        private final Map<String, List<String>> known = new HashMap<>();

        private void blink() {
            for (int i = 0; i < stones.size(); i++) {
                var stone = stones.get(i);
                if (stone.equals("0")) {
                    stones.set(i, "1");
                } else if (isEven(stone.length())) {
                    var halfLen = stone.length() / 2;
                    var firstHalf = stone.substring(0, halfLen);
                    var secondHalf = stone.substring(halfLen);
                    stones.set(i, parseNumbersFromRight(secondHalf));
                    stones.add(i++, firstHalf);
                } else {
                    stones.set(i, "" + new BigInteger(stone).multiply(MULTIPLIER));
                }
            }
        }

        private void processStone(String stone, int depth) {
            if (depth <= 0 || known.containsKey(stone))
                return;

            var result = getResult(stone);
            known.put(stone, result);
            for (var s : result)
                processStone(s, depth - 1);
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

        private List<String> getResult(String stone) {
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
    }
}
