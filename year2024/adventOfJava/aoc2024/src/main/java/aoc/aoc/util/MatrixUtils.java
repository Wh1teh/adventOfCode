package aoc.aoc.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

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
}
