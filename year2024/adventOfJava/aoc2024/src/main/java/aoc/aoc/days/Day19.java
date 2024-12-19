package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;
import aoc.aoc.util.Graph;

import java.util.*;
import java.util.stream.Collectors;

public class Day19 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + new TowelSorter(input)
                .with(Part.PART_1)
                .countPossibleDesigns();
    }

    @Override
    protected String part2Impl(String input) {
        return "" + new TowelSorter(input)
                .with(Part.PART_2)
                .countPossibleDesigns();
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
                if (part == Part.PART_1)
                    sum += designIsPossible(design) ? 1 : 0;
                else
                    sum += amountOfPossibleImplementations(design);
            }

            return sum;
        }

        private int amountOfPossibleImplementations(String design) {
            var permutations = permutationsMatrix(design);
            permutations = nullifyUnviable(permutations);

            var graph = new Graph<String>();
            for (int currentColumn = 0; currentColumn < permutations.size(); currentColumn++) {
                for (int row = 0; row < permutations.size() - currentColumn; row++) {
                    var node = permutations.get(row).get(currentColumn);
                    linkNodeToNextColumn(node, graph, currentColumn, permutations);
                }
            }

            for (var row : permutations) {
                if (row.getFirst() != null
                        && !graph.dijkstra(row.getFirst(), a -> a != null && design.endsWith(a)).isEmpty())
                    return 1;
            }

            return 0;
        }

        private void linkNodeToNextColumn(String source, Graph<String> graph, int currentColumn, List<List<String>> permutations) {
            if (source == null)
                return;

            for (var row : permutations) {
                if (row.size() <= currentColumn + 1)
                    continue;

                var destination = row.get(currentColumn + 1);
                if (destination != null)
                    graph.addEdge(source, destination, 1);
            }
        }

        private List<List<String>> nullifyUnviable(List<List<String>> permutations) {
            return permutations.stream().map(row -> row.stream()
                    .map( node -> towels.contains(node) ? node : null).toList()
            ).toList();
        }

        private boolean designIsPossible(String design) {
            var permutations = permutationsMatrix(design);

            int row = 0;
            int col = 0;
            int depth = 0;
            Deque<String> sb = new ArrayDeque<>();
            while (row < permutations.size() && stackLength(sb) < permutations.get(row).size()) {
                var next = permutations.get(row).get(col);
                var contains = towels.contains(next);
                if (contains) {
                    sb.add(next);
                    col = stackLength(sb);
                    row = 0;
                } else if (!sb.isEmpty() && col + 1 >= permutations.get(row).size()) {
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
