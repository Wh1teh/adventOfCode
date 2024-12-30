package aoc.aoc.days.implementation;

import aoc.aoc.util.*;
import lombok.experimental.StandardException;

import java.util.*;
import java.util.stream.Collectors;

public class Day06 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + getListOfVisited(GenericMatrix.charMatrix(input))
                .stream().map(Position::position)
                .collect(Collectors.toSet())
                .size();
    }

    @Override
    protected String part2Impl(String input) {
        return "" + countCycleCausingPositions(GenericMatrix.charMatrix(input));
    }

    private static final Set<Character> GUARD_CHARACTERS = Set.of('^', 'v', '<', '>');
    private static final char WALL = '#';
    public static final char FLOOR = '.';

    private record Position(Coordinate position, Direction direction) {
    }

    private static int countCycleCausingPositions(Matrix<Character> matrix) {
        var positions = getListOfVisited(matrix);

        Set<Coordinate> revisited = new HashSet<>();
        int cyclingPositions = 0;
        for (var current : positions) {
            cyclingPositions += blockingCurrentPositionCausesCycle(revisited, current, matrix) ? 1 : 0;
        }

        return cyclingPositions;
    }

    private static boolean blockingCurrentPositionCausesCycle(Set<Coordinate> revisited, Position current, Matrix<Character> matrix) {
        var next = getNextPosition(matrix, current);
        if (next == null || hasBeenVisitedBefore(revisited, next))
            return false;

        matrix.set(next.position, WALL);
        boolean result = willLoop(current, matrix);
        matrix.set(next.position, FLOOR);

        return result;
    }

    private static boolean hasBeenVisitedBefore(Set<Coordinate> revisited, Position next) {
        return !revisited.add(next.position);
    }

    private static boolean willLoop(Position current, Matrix<Character> matrix) {
        Set<Position> visited = new HashSet<>();
        while (visited.add(current)) {
            current = getNextPosition(matrix, current);
            if (current == null)
                return false;
        }

        return true;
    }

    private static List<Position> getListOfVisited(Matrix<Character> matrix) {
        var current = findAndClearInitialPosition(matrix);

        List<Position> positions = new ArrayList<>();
        do {
            positions.add(current);
            current = getNextPosition(matrix, current);
        } while (current != null);

        return positions;
    }

    private static Position getNextPosition(Matrix<Character> matrix, Position current) {
        var next = new Position(MatrixUtils.nextPosition(current.position, current.direction), current.direction);
        if (MatrixUtils.notWithinMatrix(next.position, matrix))
            return null;

        current = matrix.get(next.position) != WALL ? next : turn90Degrees(current);
        return current;
    }

    private static Position turn90Degrees(Position current) {
        return new Position(current.position, Direction.rightOf(current.direction));
    }

    private static Position findAndClearInitialPosition(Matrix<Character> matrix) {
        for (int row = 0; row < matrix.height(); row++)
            for (int col = 0; col < matrix.width(); col++)
                if (GUARD_CHARACTERS.contains(matrix.get(row, col)))
                    return new Position(new Coordinate(row, col), Direction.getDirection(matrix.get(row, col)));

        throw new GuardException("Could not locate guard's initial position");
    }

    @StandardException
    private static class GuardException extends RuntimeException {
    }
}
