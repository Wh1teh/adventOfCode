package aoc.aoc.days;

import aoc.aoc.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day25 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + new LockSolver(input)
                .countValidPairs();
    }

    @Override
    protected String part2Impl(String input) {
        return "";
    }

    private static class LockSolver {

        private final List<int[]> locks;
        private final List<int[]> keys;

        public LockSolver(String input) {
            var pair = parseLocksAndKeys(input);
            this.locks = pair.first();
            this.keys = pair.second();
        }

        public int countValidPairs() {
            int sum = 0;

            for (var lock : locks) {
                for(var key : keys) {
                    sum += isValidPair(lock,key) ? 1 : 0;
                }
            }

            return sum;
        }

        private boolean isValidPair(int[] lock, int[] key) {
            for (int i = 0; i < lock.length; i++) {
                if (key[i] + lock[i] > 5)
                    return false;
            }

            return true;
        }

        private static Pair<List<int[]>, List<int[]>> parseLocksAndKeys(String input) {
            List<int[]> locks = new ArrayList<>();
            List<int[]> keys = new ArrayList<>();

            Arrays.stream(input.trim().split("\\R{2}")).forEach(part -> {
                if (part.charAt(0) == '#')
                    locks.add(countHeight(part));
                else
                    keys.add(countHeight(part));
            });

            return new Pair<>(locks, keys);
        }

        private static int[] countHeight(String segment) {
            int[] lock = new int[5];

            var lines = segment.lines().toList();
            for (int col = 0; col < lines.getFirst().length(); col++) {
                int height = 5;
                for (int row = 1; row < lines.size() - 1; row++) {
                    if (lines.get(row).charAt(col) == '.')
                        height--;
                }
                lock[col] = height;
            }

            return lock;
        }
    }
}
