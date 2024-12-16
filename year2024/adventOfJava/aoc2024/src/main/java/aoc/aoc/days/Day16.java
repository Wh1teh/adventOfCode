package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;
import aoc.aoc.util.*;

import static aoc.aoc.days.Part.PART_1;
import static aoc.aoc.util.Direction.*;
import static aoc.aoc.util.MatrixUtils.*;

public class Day16 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + new Maze(input)
                .with(PART_1)
                .calculatePoints();
    }

    @Override
    protected String part2Impl(String input) {
        return "";
    }

    private static class Maze extends AbstractSolver<Maze> {

        record Node(Coordinate position, Direction direction, char type) {
        }

        private final Graph<Node> graph = new Graph<>();
        private Node start = null;

        public Maze(String input) {
            connectNodes(StringMatrix.matrix(input));
        }

        public int calculatePoints() {
            var path = graph.dijkstra(start, node -> node.type() == 'E');
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

                var position = new Coordinate(y, x);
                applyAdjacent(matrix, position,
                        c -> ".SE".contains("" + c),
                        (nextPosition, nextDirection) -> {
                            for (var direction : Direction.values()) {
                                addDirectionalEdge(position, nextPosition, direction, nextDirection, matrix);
                            }
                        }
                );
            });
        }

        private void addDirectionalEdge(
                Coordinate position, Coordinate nextPosition, Direction direction, Direction nextDirection, Matrix<Character> matrix) {
            var node = new Node(position, direction, matrix.get(position));
            graph.addEdge(node, new Node(nextPosition, nextDirection, matrix.get(nextPosition)), getCost(direction, nextDirection));
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
