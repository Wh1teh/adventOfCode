package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;
import aoc.aoc.util.*;

import java.util.*;

import static aoc.aoc.util.GenericMatrix.charMatrix;

public class Day21 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + new KeypadMadness().countComplexity(input, 3);
    }

    @Override
    protected String part2Impl(String input) {
        return "" + new KeypadMadness().countComplexity(input, 26);
    }

    private static class KeypadMadness extends AbstractSolver<KeypadMadness> {

        private static final String PRIORITY = " <v>^A";

        private final Graph<Character> graphNumpad;
        private final Graph<Character> graphDpad;

        private final Map<Pair<Character, Character>, Direction> directions = new HashMap<>();

        private final Map<Pair<String, Integer>, Long> outerMemo = new HashMap<>();
        private final Map<Pair<Character, Character>, List<String>> innerMemo = new HashMap<>();

        public KeypadMadness() {
            this.graphNumpad = buildKeypadGraph(charMatrix("789\n456\n123\n 0A"));
            this.graphDpad = buildKeypadGraph(charMatrix(" ^A\n<v>"));
        }

        public long countComplexity(String input, int maxDepth) {
            return input.lines().mapToLong(line -> {
                var sequence = propagateInstructions(line, 0, maxDepth);
                return sequence * numericalValue(line);
            }).sum();
        }

        private static int numericalValue(String line) {
            return Integer.parseInt(line.replace("A", ""));
        }

        private long propagateInstructions(String buttons, int depth, int maxDepth) {
            if (depth >= maxDepth)
                return buttons.length();

            var memoized = new Pair<>(buttons, depth);
            var existing = outerMemo.get(memoized);
            if (existing != null)
                return existing;

            char[] from = {'A'};
            long result = buttons.chars().mapToLong(to -> {
                long shortest = Long.MAX_VALUE;

                for (var path : getPaths(from[0], (char) to, depth == 0)) {
                    var res = propagateInstructions(path, depth + 1, maxDepth);
                    shortest = Math.min(shortest, res);
                }
                from[0] = (char) to;

                return shortest;
            }).sum();

            outerMemo.put(memoized, result);
            return result;
        }

        private List<String> getPaths(char from, char to, boolean isNumpad) {
            var memoized = new Pair<>(from, to);
            var existing = innerMemo.get(memoized);
            if (existing != null)
                return existing;

            var paths = isNumpad ?
                    graphNumpad.findEveryShortestPath(from, to) :
                    graphDpad.findEveryShortestPath(from, to);

            var result = paths.stream().map(this::pathToString).toList();
            innerMemo.put(memoized, result);
            return result;
        }

        private String pathToString(List<Character> path) {
            var sb = new StringBuilder();

            char from = '\0';
            for (Character to : path) {
                if (from != '\0')
                    sb.append(Direction.toChar(directions.get(new Pair<>(from, to))));
                from = to;
            }

            return sb.append('A').toString();
        }

        private Graph<Character> buildKeypadGraph(Matrix<Character> matrix) {
            var graph = new Graph<Character>();
            matrix.iterate((from, position) -> {
                if (from == ' ')
                    return;

                MatrixUtils.applyAdjacent(matrix, position, ch -> ch != ' ', (adjacent, direction, to) -> {
                    directions.put(new Pair<>(from, to), direction);
                    switch (direction) {
                        case UP -> graph.addEdge(from, to, PRIORITY.indexOf('^'));
                        case RIGHT -> graph.addEdge(from, to, PRIORITY.indexOf('>'));
                        case DOWN -> graph.addEdge(from, to, PRIORITY.indexOf('v'));
                        case LEFT -> graph.addEdge(from, to, PRIORITY.indexOf('<'));
                    }
                });
            });

            return graph;
        }
    }
}
