package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;

import java.util.*;
import java.util.stream.Collectors;

public class Day19 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + new TowelSorter(input)
                .countPossibleDesigns();
    }

    @Override
    protected String part2Impl(String input) {
        return "";
    }

    private static class TowelSorter extends AbstractSolver<TowelSorter> {

        private final Set<String> towels;
        private final List<String> designs;

        public TowelSorter(String input) {
            var parts = input.split("\\R{2}");
            this.towels = Arrays.stream(parts[0].split(", "))
                    .collect(Collectors.toCollection(TreeSet::new));
            this.designs = Arrays.stream(parts[1].split("\\R")).toList();
        }

        public int countPossibleDesigns() {
            int sum = 0;

            for (var design : designs) {
                sum += designIsPossible(design) ? 1 : 0;
            }

            return sum;
        }

        private boolean designIsPossible(String design) {
            var perms = permutationsMatrix(design);

            int row = 0;
            int col = 0;
            int depth = 0;
            Deque<String> sb = new ArrayDeque<>();
            while (row < perms.size() && stackLength(sb) < perms.get(row).size()) {
                var next = perms.get(row).get(col);
                var contains = towels.contains(next);
                if (contains) {
                    sb.add(next);
                    col = stackLength(sb);
                    row = 0;
                } else if (!sb.isEmpty() && col + 1 >= perms.get(row).size()) {
                    sb.removeLast();
                    col = stackLength(sb);
                    row = depth++;
                } else {
                    row++;
                }
            }

            return design.contentEquals(sb.toString()
                    .replaceAll("[\\[\\], ]", ""));
        }

        private int stackLength(Deque<String> stack) {
            return stack.stream().mapToInt(String::length).sum();
        }

        private List<List<String>> permutationsMatrix(String designLeft) {
            List<List<String>> permutations = new ArrayList<>();
            for (int span = 1; span <= designLeft.length(); span++) {
                List<String> row = new ArrayList<>();
                for (int index = 0; index + span - 1 < designLeft.length(); index++) {
                    var sb = new StringBuilder();
                    for (int i = 0; i < span; i++) {
                        sb.append(designLeft.charAt(i + index));
                    }
                    row.add(sb.toString());
                }
                permutations.add(row);
            }
            return permutations;
        }
    }
}
