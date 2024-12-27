package aoc.aoc.days;

import aoc.aoc.cache.Memoize;
import aoc.aoc.util.*;

import java.util.*;

import static aoc.aoc.util.GenericMatrix.charMatrix;

public class Day21 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + countComplexity(input, 3);
    }

    @Override
    protected String part2Impl(String input) {
        return "" + countComplexity(input, 26);
    }

    private static final String PRIORITY = " <v>^A";

    private final Map<Pair<Character, Character>, Direction> directions = new HashMap<>();
    private final Graph<Character> keypadGraph = initializeGraph();

    private long countComplexity(String input, int maxDepth) {
        return input.lines().mapToLong(line -> {
            var characterList = line.chars().mapToObj(c -> (char) c).toList();
            var sequence = propagateInstructions(characterList, 0, maxDepth);
            return sequence * numericalValue(line);
        }).sum();
    }

    private static int numericalValue(String line) {
        return Integer.parseInt(line.replace("A", ""));
    }

    @Memoize
    protected long propagateInstructions(List<Character> buttons, int depth, int maxDepth) {
        if (depth >= maxDepth)
            return buttons.size();

        char[] from = {'A'};
        return buttons.stream().mapToLong(to -> {
            long shortest = Long.MAX_VALUE;

            for (var path : getPaths(from[0], to)) {
                var res = propagateInstructions(path, depth + 1, maxDepth);
                shortest = Math.min(shortest, res);
            }
            from[0] = to;

            return shortest;
        }).sum();
    }

    @Memoize
    protected List<List<Character>> getPaths(char from, char to) {
        var paths = keypadGraph.findEveryShortestPath(from, to);
        return paths.stream().map(this::transformToDirections).toList();
    }

    private List<Character> transformToDirections(List<Character> path) {
        List<Character> nextDirections = new ArrayList<>();

        Utils.trailingIteration(path, (from, to) -> nextDirections.add(
                Direction.toChar(directions.get(new Pair<>(from, to)))
        ));

        return Utils.listAdd(nextDirections, 'A');
    }

    private Graph<Character> initializeGraph() {
        var numpadGraph = buildKeypadGraph(charMatrix("789\n456\n123\n 0A"), new Graph<>());
        return buildKeypadGraph(charMatrix(" ^A\n<v>"), numpadGraph);
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
