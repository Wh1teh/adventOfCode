package aoc.aoc.days.implementation;

import aoc.aoc.cache.Memoize;
import aoc.aoc.days.enums.Part;
import aoc.aoc.util.Graph;
import aoc.aoc.util.Utils;
import aoc.aoc.util.VisitableGraph;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day19 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + countPossibleDesigns(input);
    }

    @Override
    protected String part2Impl(String input) {
        return "" + countPossibleDesigns(input);
    }

    private long countPossibleDesigns(String input) {
        var parts = input.split("\\R{2}");
        Set<String> towels = Arrays.stream(parts[0].split(", ")).collect(Collectors.toSet());
        List<String> designs = Arrays.stream(parts[1].split("\\R")).toList();

        AtomicLong sum = new AtomicLong();
        if (part == Part.PART_1)
            Utils.forEachWithExecutorService(designs, design -> sum.addAndGet(
                    designIsPossible(design, towels) ? 1 : 0
            ));
        else
            Utils.forEachWithExecutorService(designs, design -> sum.addAndGet(
                    amountOfPossibleImplementations(design, towels)
            ));

        return sum.get();
    }

    protected record Node(String part, int col, boolean isLastPart) {
    }

    private long amountOfPossibleImplementations(String design, Set<String> towels) {
        var permutations = nullifyUnviable(permutationsMatrix(design), towels);
        var graph = buildGraphOfPossibleTraversals(permutations);

        return permutations.stream().mapToLong(row -> {
            var node = new Node(row.getFirst(), 0, false);
            return graph.acceptTraversalMethod(g ->
                    dfsCountPaths(g, node, Node::isLastPart, design)
            );
        }).sum();
    }

    @Memoize
    protected <T> long dfsCountPaths(VisitableGraph<T> graph, T current, Predicate<T> endCondition,
                                     @SuppressWarnings("unused") String idForMemo) {
        if (endCondition.test(current))
            return 1L;

        AtomicLong count = new AtomicLong(0L);
        graph.forAdjacent(current, destination -> count.addAndGet(
                dfsCountPaths(graph, destination, endCondition, idForMemo)
        ));

        return count.get();
    }

    private Graph<Node> buildGraphOfPossibleTraversals(List<List<String>> permutations) {
        var graph = new Graph<Node>();

        for (int col = 0; col < permutations.size(); col++) {
            for (int row = 0; row < permutations.size() - col; row++) {
                var node = permutations.get(row).get(col);
                linkNodeToNextColumn(graph, permutations, node, col);
            }
        }

        return graph;
    }

    private void linkNodeToNextColumn(Graph<Node> graph, List<List<String>> permutations, String source, int col) {
        if (source == null)
            return;

        int nextCol = col + source.length();
        for (var row : permutations) {
            if (row.size() <= nextCol)
                return;

            var destination = row.get(nextCol);
            boolean isLast = row.size() - 1 == nextCol;
            if (destination != null)
                graph.addEdge(
                        new Node(source, col, false),
                        new Node(destination, nextCol, isLast)
                );
        }
    }

    private List<List<String>> nullifyUnviable(List<List<String>> permutations, Set<String> towels) {
        return permutations.stream().map(row -> row.stream()
                .map(node -> towels.contains(node) ? node : null).toList()
        ).toList();
    }

    private boolean designIsPossible(String design, Set<String> towels) {
        var permutations = permutationsMatrix(design);

        int row = 0;
        int col = 0;
        int depth = 0;
        Deque<String> sb = new ArrayDeque<>();
        List<String> permutationRow;
        while (row < permutations.size() && stackLength(sb) < (permutationRow = permutations.get(row)).size()) {
            var next = permutationRow.get(col);
            var contains = towels.contains(next);
            if (contains) {
                sb.add(next);
                col = stackLength(sb);
                row = 0;
            } else if (!sb.isEmpty() && col + 1 >= permutationRow.size()) {
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
