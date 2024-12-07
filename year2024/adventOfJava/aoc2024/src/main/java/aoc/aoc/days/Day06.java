package aoc.aoc.days;

import aoc.aoc.util.Coordinate;
import aoc.aoc.util.Graph;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static aoc.aoc.util.Utils.replaceChartAt;

public class Day06 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        var guard = new Guard(input.lines().toList());

        var distinctPositions = new HashSet<>(Set.of(guard.position()));
        Coordinate position;
        while (true) {
            position = guard.move();

            if (position == null)
                break;
            else
                distinctPositions.add(position);
        }

        return "" + distinctPositions.size();
    }

    @Override
    protected String part2Impl(String input) {
        var guard = new Guard(new ArrayList<>(input.lines().toList()));

        var loopingPositions = new HashSet<Coordinate>();
        Coordinate position;
        while(true) {
            position = guard.move();
            if (position == null)
                break;
            else {
                if (guard.blockingCurrentPositionResultsInCloseLoop())
                    loopingPositions.add(position);
            }
        }

        return "" + loopingPositions.size();
    }

    @Accessors(fluent = true)
    private static class Guard {

        private enum Direction {
            UP,
            RIGHT,
            DOWN,
            LEFT;

            public static Direction getNthDirection(int n) {
                Direction[] directions = Direction.values();
                if (n < 0 || n >= directions.length) {
                    throw new IllegalArgumentException("Invalid index: " + n);
                }
                return directions[n];
            }
        }

        private static final char BLOCKER = '#';
        private static final Set<Character> GUARD_CHARACTERS = Set.of('^', 'v', '<', '>');

        private final Graph<Coordinate> graph = new Graph<>();
        private final List<String> matrix;

        private Direction direction;
        @Getter
        private Coordinate position;

        public Guard(List<String> matrix) {
            this.matrix = matrix;

            this.position = getInitialPosition();
            this.direction = getInitialDirection();
        }

        private Guard(List<String> matrix, Direction direction, Coordinate position) {
            this.matrix = matrix;
            this.direction = direction;
            this.position = position;
        }

        public Coordinate move() {
            var nextPosition = getNextPosition();

            if (notWithinMap(nextPosition)) {
                position = null;
            } else if (isBlocker(nextPosition)) {
                turn90Degrees();
            } else {
                graph.addVertex(nextPosition);
                graph.addEdge(position, nextPosition);
                position = nextPosition;
            }

            return position;
        }

        public int countCycles() {
            return graph.countCycles();
        }

        public void printGraph(){
            graph.printGraph();
        }

        public boolean blockingCurrentPositionResultsInCloseLoop() {
            boolean result = false;

            var nextPosition = getNextPosition();
            if (notWithinMap(nextPosition) || isBlocker(nextPosition))
                return result;

            replaceNextPosition(nextPosition, BLOCKER);

            var ghost = new Guard(matrix, direction, position);
            ghost.turn90Degrees();

            Coordinate ghostPosition;
            var set = new HashSet<Coordinate>();
            while (!result) {
                ghostPosition = ghost.move();
                if (ghostPosition == null)
                    break;
                else if (set.contains(ghostPosition) && isBlocker(ghost.getNextPosition())) {
                    result = true;
                } else
                    set.add(ghostPosition);
            }


            replaceNextPosition(nextPosition, '.');
            return result;
        }

        private void turn90Degrees() {
            int nextOrdinal = direction.ordinal() + 1;
            direction = Direction.getNthDirection(
                    nextOrdinal % Direction.values().length
            );
        }

        private Coordinate getInitialPosition() {
            for (int i = 0; i < matrix.size(); i++) {
                String line = matrix.get(i);
                for (int j = 0; j < line.length(); j++) {
                    if(GUARD_CHARACTERS.contains(line.charAt(j)))
                        return new Coordinate(i, j);
                }
            }

            return null;
        }

        private Coordinate getNextPosition() {
            return switch (direction) {
                case UP -> new Coordinate(position.y() - 1, position.x());
                case DOWN -> new Coordinate(position.y() + 1, position.x());
                case LEFT -> new Coordinate(position.y(), position.x() - 1);
                case RIGHT -> new Coordinate(position.y(), position.x() + 1);
            };
        }

        private void replaceNextPosition(Coordinate position, char with) {
            int y = position.y();
            String result = replaceChartAt(matrix.get(y), position.x(), with);
            matrix.set(y, result);
        }

        private Direction getInitialDirection() {
            char ch = matrix
                    .get(position.y())
                    .charAt(position.x());
            return switch (ch) {
                case '^' -> Direction.UP;
                case '>' -> Direction.RIGHT;
                case 'v' -> Direction.DOWN;
                case '<' -> Direction.LEFT;
                default -> throw new IllegalStateException("Unexpected value: " + ch);
            };
        }

        private boolean isBlocker(Coordinate coordinate) {
            return matrix.get(coordinate.y()).charAt(coordinate.x()) == BLOCKER;
        }

        private boolean notWithinMap(Coordinate position) {
            return position.y() < 0 || position.y() >= matrix.size()
                    || position.x() < 0 || position.x() >= matrix.getFirst().length();
        }
    }
}
