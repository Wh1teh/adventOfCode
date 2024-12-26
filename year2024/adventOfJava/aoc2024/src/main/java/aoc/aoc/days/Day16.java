package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;
import aoc.aoc.util.*;

import java.util.*;

import static aoc.aoc.util.Direction.*;
import static aoc.aoc.util.MatrixUtils.*;

public class Day16 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + new Maze(input)
                .calculatePoints();
    }

    @Override
    protected String part2Impl(String input) {
        return "" + new Maze(input)
                .calculateBestTiles();
    }

    private static class Maze extends AbstractSolver<Maze> {

        record Node(Coordinate position, Direction direction, char type) {
        }

        private final Graph<Node> graph = new Graph<>();
        private Node start = null;

        public Maze(String input) {
            connectNodes(StringMatrix.matrix(input));
        }

        public int calculateBestTiles() {
            var paths = graph.dijkstraEveryPath(start, node -> node.type() == 'E');

            TreeMap<Integer, List<List<Node>>> sorted = new TreeMap<>();
            for(var sets : paths) {
                for (var path : sets) {
                    int points = countPoints(path);
                    sorted.computeIfAbsent(points, __ -> new ArrayList<>()).add(path);
                }
            }

            Set<Node> uniqueTiles = new TreeSet<>(Comparator.comparing(a -> a.position));
            sorted.firstEntry().getValue().forEach(uniqueTiles::addAll);

            return uniqueTiles.size();
        }

        public int calculatePoints() {
            var path = graph.dijkstra(start, node -> node.type() == 'E');
            return countPoints(path);
        }

        private int countPoints(List<Node> path) {
            int sum = 0;
            Node prev = null;
            for (var next : path) {
                if (prev == null) {
                    prev = next;
                    continue;
                }

                int cost = getCost(prev.direction, next.direction);
                sum += cost;
                prev = next;
            }

            return sum;
        }

        private void connectNodes(Matrix<Character> matrix) {
            matrix.iterate((character, y, x) -> {
                if (character == '#')
                    return;
                if (character == 'S')
                    this.start = new Node(new Coordinate(y, x), RIGHT, 'S');

                var from = new Coordinate(y, x);
                applyAdjacent(matrix, from, c -> ".SE".contains("" + c),
                        (to, toDirection) -> Arrays.stream(values())
                                .forEach(fromDirection -> graph.addEdge(
                                        new Node(from, fromDirection, matrix.get(from)),
                                        new Node(to, toDirection, matrix.get(to)),
                                        getCost(fromDirection, toDirection)
                                )));
            });
        }

        private int getCost(Direction original, Direction next) {
            if (oppositeOf(original) == next)
                return 2002;
            else if (rightOf(original) == next || leftOf(original) == next)
                return 1001;
            else
                return 1;
        }
    }
}
