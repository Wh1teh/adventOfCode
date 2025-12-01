package aoc.aoc.util;

import lombok.Getter;

import java.util.Map;
import java.util.HashMap;

@Getter
public enum Direction {
    UP('^', 'U'),
    RIGHT('>', 'R'),
    DOWN('v', 'D'),
    LEFT('<', 'L');

    private final char arrowChar;
    private final char shortChar;

    Direction(char arrowChar, char shortChar) {
        this.arrowChar = arrowChar;
        this.shortChar = shortChar;
    }

    private static final Map<Character, Direction> lookup = new HashMap<>();

    static {
        for (Direction d : values()) {
            lookup.put(d.arrowChar, d);
            lookup.put(Character.toUpperCase(d.shortChar), d);
            lookup.put(Character.toLowerCase(d.shortChar), d);
        }
    }

    public static Direction getDirection(char ch) {
        Direction d = lookup.get(ch);
        if (d == null) throw new IllegalStateException("Unexpected value: " + ch);
        return d;
    }

    public static char toChar(Direction direction) {
        return direction.arrowChar;
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
