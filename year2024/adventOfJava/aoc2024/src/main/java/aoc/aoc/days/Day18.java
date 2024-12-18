package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;
import aoc.aoc.util.*;
import lombok.experimental.StandardException;

import java.util.*;

public class Day18 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        int size = input.length() >= 1000 ? 71 : 7;
        int steps = input.length() >= 1000 ? 1024 : 12;
        return "" + (new Corrupter(input, size)
                .with(Part.PART_1)
                .findShortestPathAfterSteps(steps)
                .size() - 1);
    }

    @Override
    protected String part2Impl(String input) {
        int size = input.length() >= 1000 ? 71 : 7;
        var result = new Corrupter(input, size)
                .with(Part.PART_2)
                .binarySearchFirstToBlock();
        return "%d,%d".formatted(result.x(), result.y());
    }

    private static class Corrupter extends AbstractSolver<Corrupter> {

        private final Graph<Coordinate> graph = new Graph<>();
        private final Matrix<Boolean> matrix;
        private final List<Coordinate> futureLocations;

        public Corrupter(String input, int widthAndHeight) {
            this.matrix = new GenericMatrix<>(() -> Boolean.FALSE, widthAndHeight);
            this.futureLocations = parseInput(input);
        }

        public void simulateSteps(int steps) {
            var iterator = futureLocations.iterator();
            for (int i = 0; i < steps; i++) {
                matrix.set(iterator.next(), true);
            }
        }

        public Coordinate binarySearchFirstToBlock() {
            int left = 0;
            int right = futureLocations.size() - 1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                int direction = circleInOnFirstBlockedPosition(mid);

                if (direction == 0)
                    return futureLocations.get(mid - 1);
                else if (direction < 0)
                    left = mid + 1;
                else
                    right = mid - 1;
            }

            throw new BinarySearchFailedException();
        }

        private int circleInOnFirstBlockedPosition(int steps) {
            var path = findShortestPathAfterSteps(steps);
            if (path.isEmpty())
                return !findShortestPathAfterSteps(steps - 1).isEmpty() ? 0 : 1;
            return -1;
        }

        private List<Coordinate> findShortestPathAfterSteps(int steps) {
            resetMatrix();
            simulateSteps(steps);

            graph.clear();
            return calculateShortestPath();
        }

        private void resetMatrix() {
            matrix.iterate((__, y, x) -> matrix.set(y, x, false));
        }

        public List<Coordinate> calculateShortestPath() {
            buildGraph();
            var start = new Coordinate(0, 0);
            var end = new Coordinate(matrix.size() - 1, matrix.size() - 1);

            return graph.dijkstra(start, position -> position.equals(end));
        }

        private void buildGraph() {
            matrix.iterate((isCorrupted, position) -> {
                if (Boolean.TRUE.equals(isCorrupted))
                    return;

                MatrixUtils.applyAdjacent(matrix, position,
                        Boolean.FALSE::equals,
                        adjacent -> graph.addEdge(position, adjacent, 1));
            });
        }

        private List<Coordinate> parseInput(String input) {
            return input.lines()
                    .map(line -> {
                        var parts = line.split(",");
                        return new Coordinate(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
                    })
                    .toList();
        }

        @StandardException
        private static class BinarySearchFailedException extends RuntimeException {
        }
    }
}
