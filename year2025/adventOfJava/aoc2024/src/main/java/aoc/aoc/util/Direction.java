package aoc.aoc.util;

public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public static Direction getDirection(char ch) {
        return switch (ch) {
            case '^' -> UP;
            case '>' -> RIGHT;
            case 'v' -> DOWN;
            case '<' -> LEFT;

            default -> throw new IllegalStateException("Unexpected value: " + ch);
        };
    }

    public static char toChar(Direction direction) {
        return switch (direction) {
            case UP -> '^';
            case RIGHT -> '>';
            case DOWN -> 'v';
            case LEFT -> '<';
        };
    }

    public static Direction oppositeOf(Direction direction) {
        return switch (direction) {
            case UP -> DOWN;
            case RIGHT -> LEFT;
            case DOWN -> UP;
            case LEFT -> RIGHT;
        };
    }

    public static Direction leftOf(Direction direction) {
        return switch (direction) {
            case UP -> LEFT;
            case RIGHT -> UP;
            case DOWN -> RIGHT;
            case LEFT -> DOWN;
        };
    }

    public static Direction rightOf(Direction direction) {
        return switch (direction) {
            case UP -> RIGHT;
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
        };
    }
}
