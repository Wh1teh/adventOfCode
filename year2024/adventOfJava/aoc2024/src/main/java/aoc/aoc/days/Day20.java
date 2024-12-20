package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;
import aoc.aoc.util.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day20 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + new CheatingMazeSolver(input)
                .countTimeSavingCheatsWhenRange(isSample ? 12 : 100, 2);
    }

    @Override
    protected String part2Impl(String input) {
        return "" + new CheatingMazeSolver(input)
                .countTimeSavingCheatsWhenRange(isSample ? 66 : 100, 20);
    }

    private static class CheatingMazeSolver extends AbstractSolver<CheatingMazeSolver> {

        private final Matrix<Character> matrix;

        private final Coordinate start;
        private final Coordinate end;

        public CheatingMazeSolver(String input) {
            this.matrix = GenericMatrix.charMatrix(input);

            var startAndEnd = parseStartAndEnd(matrix);
            this.start = startAndEnd.first();
            this.end = startAndEnd.second();
        }

        public int countTimeSavingCheatsWhenRange(int picosecondsToSave, int range) {
            var path = getBasePath();
            return runCheatScanForEveryPosition(range, path).entrySet().stream()
                    .mapToInt(e -> e.getKey() >= picosecondsToSave ? e.getValue() : 0)
                    .sum();
        }

        private Map<Integer, Integer> runCheatScanForEveryPosition(int range, Map<Coordinate, Integer> path) {
            var saves = new ConcurrentHashMap<Integer, Integer>();

            Utils.forEachWithExecutorService(path.keySet(),
                    position -> getSavesInRange(path, position, range).forEach((timeSaved, instancesOf) ->
                            saves.compute(timeSaved, (k, v) ->
                                    v == null ? instancesOf : v + instancesOf
                            ))
            );

            return saves;
        }

        private Map<Coordinate, Integer> getBasePath() {
            var graph = buildGraph();
            var index = new AtomicInteger(0);
            return graph.dijkstra(start, end::equals).stream()
                    .collect(Collectors.toMap(
                            position -> position,
                            position -> index.getAndIncrement()));
        }

        private Map<Integer, Integer> getSavesInRange(Map<Coordinate, Integer> path, Coordinate position, int range) {
            var mappedSaves = new TreeMap<Integer, Integer>();

            final int currentTime = path.get(position);
            var visited = diamondScan(position, range, false);
            visited.forEach(visitedPosition -> {
                if (matrix.get(visitedPosition) == '#')
                    return;

                int distanceFromPosition = position.distanceFromPosition(visitedPosition);
                int savedTime = path.get(visitedPosition) - currentTime - distanceFromPosition;
                if (savedTime > 1)
                    mappedSaves.compute(savedTime, (k, v) -> {
                        if (v == null)
                            v = 0;
                        return ++v;
                    });
            });

            return mappedSaves;
        }

        /**
         * <pre>
         * ..x..  2..<3
         * .xxx.  1..<4
         * xxxxx  0..<5
         * .xxx.  1..<4
         * ..x..  2..<3
         * </pre>
         */
        private Set<Coordinate> diamondScan(Coordinate start, int range, boolean useRecursion) {
            return useRecursion ?
                    diamondScanRecursive(start, start, range, 0, new TreeSet<>()) :
                    diamondScanIterative(start, range);
        }

        private Set<Coordinate> diamondScanRecursive(
                Coordinate start, Coordinate position, int range, int travelled, Set<Coordinate> visited
        ) {
            if (start.distanceFromPosition(position) > range || visited.contains(position))
                return visited;

            visited.add(position);
            MatrixUtils.applyAdjacent(matrix, position, __ -> true,
                    adjacent -> diamondScanRecursive(start, adjacent, range, travelled + 1, visited));

            return visited;
        }

        private Set<Coordinate> diamondScanIterative(Coordinate start, int range) {
            Set<Coordinate> visited = new TreeSet<>();

            for (int row = range * -1; row <= range && start.y() + row < matrix.size(); row++) {
                if (row + start.y() < 0)
                    continue;

                int colOffset = range - Math.abs(row);
                for (int col = colOffset * -1; col <= colOffset && start.x() + col < matrix.size(); col++) {
                    if (col + start.x() < 0)
                        continue;

                    var position = new Coordinate(start.y() + row, start.x() + col);
                    visited.add(position);
                }
            }

            return visited;
        }

        private Graph<Coordinate> buildGraph() {
            var graph = new Graph<Coordinate>();

            matrix.iterate((character, from) -> {
                if (character != '#') {
                    MatrixUtils.applyAdjacent(
                            matrix, from,
                            ch -> ch != '#',
                            to -> graph.addEdge(from, to, 1)
                    );
                }
            });

            return graph;
        }

        private static Pair<Coordinate, Coordinate> parseStartAndEnd(Matrix<Character> matrix) {
            var startAndEnd = new Pair<Coordinate, Coordinate>();

            matrix.iterate((character, y, x) -> {
                if (character == 'S')
                    startAndEnd.first(new Coordinate(y, x));
                else if (character == 'E')
                    startAndEnd.second(new Coordinate(y, x));
            });

            return startAndEnd;
        }
    }
}
