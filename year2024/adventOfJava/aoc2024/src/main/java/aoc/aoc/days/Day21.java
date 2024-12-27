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

        private final Graph<Character> keypadGraph;

        private final Map<Pair<Character, Character>, Direction> directions = new HashMap<>();

        private final Map<Pair<List<Character>, Integer>, Long> outerMemo = new HashMap<>();
        private final Map<Pair<Character, Character>, List<List<Character>>> innerMemo = new HashMap<>();

        public KeypadMadness() {
            var numpadGraph = buildKeypadGraph(charMatrix("789\n456\n123\n 0A"), new Graph<>());
            this.keypadGraph = buildKeypadGraph(charMatrix(" ^A\n<v>"), numpadGraph);
        }

        public long countComplexity(String input, int maxDepth) {
            return input.lines().mapToLong(line -> {
                var characterList = line.chars().mapToObj(c -> (char) c).toList();
                var sequence = propagateInstructions(characterList, 0, maxDepth);
                return sequence * numericalValue(line);
            }).sum();
        }

        private static int numericalValue(String line) {
            return Integer.parseInt(line.replace("A", ""));
        }

        private long propagateInstructions(List<Character> buttons, int depth, int maxDepth) {
            if (depth >= maxDepth)
                return buttons.size();

            var memoized = new Pair<>(buttons, depth);
            var existing = outerMemo.get(memoized);
            if (existing != null)
                return existing;

            char[] from = {'A'};
            long result = buttons.stream().mapToLong(to -> {
                long shortest = Long.MAX_VALUE;

                for (var path : getPaths(from[0], to)) {
                    var res = propagateInstructions(path, depth + 1, maxDepth);
                    shortest = Math.min(shortest, res);
                }
                from[0] = to;

                return shortest;
            }).sum();

            outerMemo.put(memoized, result);
            return result;
        }

        private List<List<Character>> getPaths(char from, char to) {
            var memoized = new Pair<>(from, to);
            var existing = innerMemo.get(memoized);
            if (existing != null)
                return existing;

            var paths = keypadGraph.findEveryShortestPath(from, to);

            var result = paths.stream().map(this::transformToDirections).toList();
            innerMemo.put(memoized, result);
            return result;
        }

        private List<Character> transformToDirections(List<Character> path) {
            List<Character> sb = new ArrayList<>();

            Utils.trailingIteration(path, (from, to) -> sb.add(
                    Direction.toChar(directions.get(new Pair<>(from, to)))
            ));

            return Utils.listAdd(sb, 'A');
        }

        private Graph<Character> buildKeypadGraph(Matrix<Character> matrix, Graph<Character> graph) {
            matrix.iterate((from, position) -> {
                if (from == ' ')
                    return;

                MatrixUtils.applyAdjacent(matrix, position, (direction, to) -> {
                    if (to == ' ')
                        return;

                    directions.put(new Pair<>(from, to), direction);
                    graph.addEdge(from, to, PRIORITY.indexOf(Direction.toChar(direction)));
                });
            });

            return graph;
        }
    }
}
