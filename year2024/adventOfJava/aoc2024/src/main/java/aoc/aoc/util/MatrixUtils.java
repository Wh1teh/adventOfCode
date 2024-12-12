package aoc.aoc.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MatrixUtils {

    public record Perimeter(int left, int right, int up, int down) {
    }

    public static Perimeter createPerimeter(Coordinate position, int offset, int oneToOneLimit) {
        return createPerimeter(position, offset, oneToOneLimit, oneToOneLimit);
    }

    public static Perimeter createPerimeter(Coordinate position, int offset, int verticalLimit, int horizontalLimit) {
        int up = position.y() - offset;
        int down = position.y() + offset;
        int left = position.x() - offset;
        int right = position.x() + offset;

        if (up < 0) {
            up = 0;
        }
        if (down >= verticalLimit) {
            down = verticalLimit - 1;
        }
        if (left < 0) {
            left = 0;
        }
        if (right >= horizontalLimit) {
            right = horizontalLimit - 1;
        }

        return new Perimeter(left, right, up, down);
    }

    public static boolean isWithinMatrix(Coordinate position, List<String> matrix) {
        return !notWithinMatrix(position, matrix);
    }

    public static boolean notWithinMatrix(Coordinate position, List<String> matrix) {
        return position.y() < 0 || position.y() >= matrix.size()
                || position.x() < 0 || position.x() >= matrix.getFirst().length();
    }

    public static <T> void applyAdjacent(
            Matrix<T> matrix, Coordinate position,
            Predicate<T> predicate,
            Consumer<Coordinate> action
    ) {
        applyTo(matrix, position.y() - 1, position.x(), predicate, action);
        applyTo(matrix, position.y(), position.x() - 1, predicate, action);
        applyTo(matrix, position.y() + 1, position.x(), predicate, action);
        applyTo(matrix, position.y(), position.x() + 1, predicate, action);
    }

    private static <T> void applyTo(
            Matrix<T> matrix, int y, int x,
            Predicate<T> predicate,
            Consumer<Coordinate> action
    ) {
        if (y < 0 || x < 0 || y >= matrix.size() || x >= matrix.size())
            return;

        T ch = matrix.get(y, x);
        if (predicate.test(ch))
            action.accept(new Coordinate(y, x));
    }

    public static <T> void applyAdjacentIncludeOutOfBounds(
            Matrix<T> matrix, Coordinate position,
            Predicate<T> predicate,
            Consumer<Coordinate> action
    ) {
        applyAdjacentIncludeOutOfBounds(matrix, position, predicate, action, action);
    }

    public static <T> void applyAdjacentIncludeOutOfBounds(
            Matrix<T> matrix, Coordinate position,
            Predicate<T> predicate,
            Consumer<Coordinate> action, Consumer<Coordinate> outOfBoundsFallback
    ) {
        applyToIncludeOutOfBounds(matrix, position.y() - 1, position.x(), predicate, action, outOfBoundsFallback);
        applyToIncludeOutOfBounds(matrix, position.y(), position.x() - 1, predicate, action, outOfBoundsFallback);
        applyToIncludeOutOfBounds(matrix, position.y() + 1, position.x(), predicate, action, outOfBoundsFallback);
        applyToIncludeOutOfBounds(matrix, position.y(), position.x() + 1, predicate, action, outOfBoundsFallback);
    }

    private static <T> void applyToIncludeOutOfBounds(
            Matrix<T> matrix, int y, int x,
            Predicate<T> predicate,
            Consumer<Coordinate> action, Consumer<Coordinate> outOfBoundsFallback) {
        if (y < 0 || x < 0 || y >= matrix.size() || x >= matrix.size()) {
            outOfBoundsFallback.accept(new Coordinate(y, x));
            return;
        }

        T ch = matrix.get(y, x);
        if (predicate.test(ch))
            action.accept(new Coordinate(y, x));
    }
}
