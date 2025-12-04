package aoc.aoc.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.function.TriConsumer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
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

    public static <T> boolean isWithinMatrix(Coordinate position, Matrix<T> matrix) {
        return !notWithinMatrix(position, matrix);
    }

    public static <T> boolean isWithinMatrix(int y, int x, Matrix<T> matrix) {
        return !notWithinMatrix(y, x, matrix);
    }

    public static boolean notWithinMatrix(Coordinate position, List<String> matrix) {
        return position.y() < 0 || position.y() >= matrix.size()
                || position.x() < 0 || position.x() >= matrix.getFirst().length();
    }

    public static <T> boolean notWithinMatrix(Coordinate position, Matrix<T> matrix) {
        return notWithinMatrix(position.y(), position.x(), matrix);
    }

    public static <T> boolean notWithinMatrix(int y, int x, Matrix<T> matrix) {
        return y < 0 || y >= matrix.height()
                || x < 0 || x >= matrix.width();
    }

    public static <T> void applyAround(
            Matrix<T> matrix, Coordinate position,
            Predicate<T> predicate,
            Consumer<Coordinate> action
    ) {
        int padding = 1;

        int yStart = Math.max(position.y() - padding, 0);
        int yEnd = Math.min(position.y() + padding, matrix.height() - 1);
        int xStart = Math.max(position.x() - padding, 0);
        int xEnd = Math.min(position.x() + padding, matrix.width() - 1);

        for (int y = yStart; y <= yEnd; y++) {
            for (int x = xStart; x <= xEnd; x++) {
                if (y == position.y() && x == position.x())
                    continue;
                applyTo(matrix, y, x, predicate, action);
            }
        }
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
        if (notWithinMatrix(y, x, matrix))
            return;

        T ch = matrix.get(y, x);
        if (predicate.test(ch))
            action.accept(new Coordinate(y, x));
    }

    public static <T> void applyAdjacent(
            Matrix<T> matrix, Coordinate position,
            Predicate<T> predicate,
            BiConsumer<Coordinate, Direction> action
    ) {
        applyTo(matrix, position.y() - 1, position.x(), predicate, action, Direction.UP);
        applyTo(matrix, position.y(), position.x() - 1, predicate, action, Direction.LEFT);
        applyTo(matrix, position.y() + 1, position.x(), predicate, action, Direction.DOWN);
        applyTo(matrix, position.y(), position.x() + 1, predicate, action, Direction.RIGHT);
    }

    private static <T> void applyTo(
            Matrix<T> matrix, int y, int x,
            Predicate<T> predicate,
            BiConsumer<Coordinate, Direction> action,
            Direction direction
    ) {
        if (notWithinMatrix(y, x, matrix))
            return;

        T ch = matrix.get(y, x);
        if (predicate.test(ch))
            action.accept(new Coordinate(y, x), direction);
    }

    public static <T> void applyAdjacent(
            Matrix<T> matrix, Coordinate position,
            Predicate<T> predicate,
            TriConsumer<Coordinate, Direction, T> action
    ) {
        applyTo(matrix, position.y() - 1, position.x(), predicate, action, Direction.UP);
        applyTo(matrix, position.y(), position.x() - 1, predicate, action, Direction.LEFT);
        applyTo(matrix, position.y() + 1, position.x(), predicate, action, Direction.DOWN);
        applyTo(matrix, position.y(), position.x() + 1, predicate, action, Direction.RIGHT);
    }

    private static <T> void applyTo(
            Matrix<T> matrix, int y, int x,
            Predicate<T> predicate,
            TriConsumer<Coordinate, Direction, T> action,
            Direction direction
    ) {
        if (notWithinMatrix(y, x, matrix))
            return;

        T ch = matrix.get(y, x);
        if (predicate.test(ch))
            action.accept(new Coordinate(y, x), direction, ch);
    }

    public static <T> void applyAdjacent(
            Matrix<T> matrix, Coordinate position,
            BiConsumer<Direction, T> action
    ) {
        applyTo(matrix, position.y() - 1, position.x(), action, Direction.UP);
        applyTo(matrix, position.y(), position.x() - 1, action, Direction.LEFT);
        applyTo(matrix, position.y() + 1, position.x(), action, Direction.DOWN);
        applyTo(matrix, position.y(), position.x() + 1, action, Direction.RIGHT);
    }

    private static <T> void applyTo(
            Matrix<T> matrix, int y, int x,
            BiConsumer<Direction, T> action,
            Direction direction
    ) {
        if (notWithinMatrix(y, x, matrix))
            return;

        action.accept(direction, matrix.get(y, x));
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
        if (y < 0 || x < 0 || y >= matrix.width() || x >= matrix.width()) {
            outOfBoundsFallback.accept(new Coordinate(y, x));
            return;
        }

        T ch = matrix.get(y, x);
        if (predicate.test(ch))
            action.accept(new Coordinate(y, x));
    }

    public static <T> int countAdjacent(Matrix<T> matrix, Coordinate position, Predicate<T> predicate) {
        var atomic = new AtomicInteger(0);
        MatrixUtils.applyAdjacent(matrix, position, predicate, ignore -> atomic.incrementAndGet());
        return atomic.get();
    }

    public static <T> T rightOf(Matrix<T> matrix, Coordinate position) {
        return notWithinMatrix(position.y(), position.x() + 1, matrix) ? null
                : matrix.get(position.y(), position.x() + 1);
    }

    public static <T> T rightOf(Matrix<T> matrix, int y, int x) {
        return notWithinMatrix(y, x + 1, matrix) ? null : matrix.get(y, x + 1);
    }

    public static <T> T leftOf(Matrix<T> matrix, Coordinate position) {
        return notWithinMatrix(position.y(), position.x() - 1, matrix) ? null
                : matrix.get(position.y(), position.x() - 1);
    }

    public static <T> T leftOf(Matrix<T> matrix, int y, int x) {
        return notWithinMatrix(y, x - 1, matrix) ? null : matrix.get(y, x - 1);
    }

    public static <T> T aboveOf(Matrix<T> matrix, Coordinate position) {
        return notWithinMatrix(position.y() - 1, position.x(), matrix) ? null
                : matrix.get(position.y() - 1, position.x());
    }

    public static <T> T aboveOf(Matrix<T> matrix, int y, int x) {
        return notWithinMatrix(y - 1, x, matrix) ? null : matrix.get(y - 1, x);
    }

    public static <T> T belowOf(Matrix<T> matrix, Coordinate position) {
        return notWithinMatrix(position.y() + 1, position.x(), matrix) ? null
                : matrix.get(position.y() + 1, position.x());
    }

    public static <T> T belowOf(Matrix<T> matrix, int y, int x) {
        return notWithinMatrix(y + 1, x, matrix) ? null : matrix.get(y + 1, x);
    }

    public static Coordinate nextPosition(Coordinate position, Direction direction) {
        int y = position.y();
        int x = position.x();
        return switch (direction) {
            case UP -> new Coordinate(y - 1, x);
            case RIGHT -> new Coordinate(y, x + 1);
            case DOWN -> new Coordinate(y + 1, x);
            case LEFT -> new Coordinate(y, x - 1);
        };
    }

    public static Direction directionOf(Coordinate position, Coordinate of) {
        if (position.x() < of.x())
            return Direction.RIGHT;
        if (position.x() > of.x())
            return Direction.LEFT;
        if (position.y() < of.y())
            return Direction.DOWN;
        if (position.y() > of.y())
            return Direction.UP;
        return null;
    }
}
