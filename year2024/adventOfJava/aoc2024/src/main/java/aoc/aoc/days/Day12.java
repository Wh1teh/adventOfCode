package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;
import aoc.aoc.util.Coordinate;
import aoc.aoc.util.Graph;
import aoc.aoc.util.Matrix;
import aoc.aoc.util.MatrixUtils;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static aoc.aoc.days.Part.PART_1;
import static aoc.aoc.days.Part.PART_2;
import static aoc.aoc.util.StringMatrix.matrix;

public class Day12 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + new Garden(matrix(input))
                .with(PART_1)
                .countPrice();
    }

    @Override
    protected String part2Impl(String input) {
        return "" + new Garden(matrix(input))
                .with(PART_2)
                .countPrice();
    }

    @RequiredArgsConstructor
    private static class Garden extends AbstractSolver<Garden> {

        private final Matrix<Character> matrix;
        private final Graph<Coordinate> graph = new Graph<>();

        public int countPrice() {
            buildGraph();
            Set<Set<Coordinate>> regions = buildRegions();

            var countedRegions = part == PART_1 ? countFences(regions) : countEdges(regions);

            return countedRegions.entrySet().stream()
                    .mapToInt(set -> set.getKey().size() * set.getValue().get())
                    .sum();
        }

        private Map<Set<Coordinate>, AtomicInteger> countEdges(Set<Set<Coordinate>> regions) {
            Map<Set<Coordinate>, AtomicInteger> countedRegions = new HashMap<>();

            regions.forEach(region -> {
                Set<Coordinate> fences = new HashSet<>();
                region.forEach(position -> {
                    char ch = matrix.get(position);
                    MatrixUtils.applyAdjacentIncludeOutOfBounds(matrix, position,
                            character -> !character.equals(ch),
                            fences::add);
                });

                var edges = new AtomicInteger();
                Map<Integer, Set<Integer>> up = new TreeMap<>();
                Map<Integer, Set<Integer>> down = new TreeMap<>();
                Map<Integer, Set<Integer>> left = new TreeMap<>();
                Map<Integer, Set<Integer>> right = new TreeMap<>();
                fences.forEach(fence -> {
                    int row = fence.y();
                    int col = fence.x();
                    indexToPlane(region, up, row, col, -1, 0);
                    indexToPlane(region, down, row, col, 1, 0);
                    indexToPlane(region, left, row, col, 0, -1);
                    indexToPlane(region, right, row, col, 0, 1);
                });

                countEdges(up, edges);
                countEdges(down, edges);
                countEdges(left, edges);
                countEdges(right, edges);

                countedRegions.put(region, edges);
            });

            return countedRegions;
        }

        private static void countEdges(Map<Integer, Set<Integer>> up, AtomicInteger edges) {
            up.forEach((k, v) -> {
                var previousIndex = new AtomicInteger(Integer.MIN_VALUE);
                v.forEach(index -> {
                    if (!index.equals(previousIndex.incrementAndGet())) {
                        edges.incrementAndGet();
                    }
                    previousIndex.set(index);
                });
            });
        }

        private static void indexToPlane(Set<Coordinate> region, Map<Integer, Set<Integer>> direction,
                                         int row, int col, int offsetRow, int offsetCol) {

            direction.compute(offsetCol == 0 ? row : col, (k, v) -> {
                if (v == null)
                    v = new TreeSet<>();

                if (region.contains(new Coordinate(row + offsetRow, col + offsetCol)))
                    v.add(offsetCol == 0 ? col : row);

                return v;
            });
        }

        @SuppressWarnings("java:S117")
        private Map<Set<Coordinate>, AtomicInteger> countFences(Set<Set<Coordinate>> regions) {
            Map<Set<Coordinate>, AtomicInteger> countedRegions = new HashMap<>();

            regions.forEach(region -> {
                var fences = new AtomicInteger();
                countedRegions.put(region, fences);

                region.forEach(position -> {
                    char ch = matrix.get(position);
                    MatrixUtils.applyAdjacentIncludeOutOfBounds(matrix, position,
                            character -> !character.equals(ch),
                            __ -> fences.incrementAndGet());
                });
            });

            return countedRegions;
        }

        private Set<Set<Coordinate>> buildRegions() {
            Set<Set<Coordinate>> regions = new HashSet<>();
            Set<Coordinate> visited = new HashSet<>();
            matrix.iterate((character, y, x) -> {
                var position = new Coordinate(y, x);

                if (visited.contains(position))
                    return;
                visited.add(position);

                regions.add(graph.allReachableFrom(position));
            });
            return regions;
        }

        private void buildGraph() {
            matrix.iterate((character, y, x) -> {
                var position = new Coordinate(y, x);
                graph.addVertex(position);
                MatrixUtils.applyAdjacent(matrix, position,
                        character::equals,
                        nextPosition -> graph.addEdge(position, nextPosition));
            });
        }
    }
}
