package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;
import aoc.aoc.util.Coordinate;
import aoc.aoc.util.Quad;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Day21 extends AbstractDay {

    // 166248 too high
    @Override
    protected String part1Impl(String input) {
        var a = new KeypadMadness().start(input);
        return "" + a;
    }

    @Override
    protected String part2Impl(String input) {
        return "";
    }

    private static class KeypadMadness extends AbstractSolver<KeypadMadness> {

        private long start(String input) {
            var atomic = new AtomicLong(0L);

            input.lines().forEach(line -> {
                var sb = new StringBuilder();
                char previous = 'A';
                for (char ch : line.toCharArray()) {
                    var dh = propagate(previous, ch, 0, 3, true);
                    var dv = propagate(previous, ch, 0, 3, false);
                    previous = ch;
                    sb.append(dh.length() < dv.length() ? dh : dv);
                }
                var numerical = numericalValue(line);
                var shortest = sb.length();
                var result = shortest * numerical;
                System.out.println("%d * %d = %d".formatted(shortest, numerical, result));
                atomic.addAndGet(result);
            });
            System.out.println();

            return atomic.get();
        }

        private int numericalValue(String line) {
            return Integer.parseInt(line.replace("A", ""));
        }

//        private final Map<Quad<Character, Character, Integer, Boolean>, String> propagationMemo = new HashMap<>();
        private String propagate(char from, char to, int depth, int maxDepth, boolean horizontalFirst) {
            horizontalFirst = avoidTheGap(from, depth == 0, horizontalFirst);

            var directions = getDirections(from, to, depth == 0, horizontalFirst);
            assert directions.endsWith("A");
            if (depth >= maxDepth - 1)
                return directions;

            var accumulate = new StringBuilder();
            char previous = 'A';
            for (char direction : directions.toCharArray()) {
                var dh = propagate(previous, direction, depth + 1, maxDepth, true);
                var dv = propagate(previous, direction, depth + 1, maxDepth, false);
                previous = direction;

                if (dh.length() < dv.length())
                    accumulate.append(dh);
                else
                    accumulate.append(dv);
            }

            return accumulate.toString();
        }

        private static boolean avoidTheGap(char from, boolean isNumpad, boolean horizontalFirst) {
            if (isNumpad) {
                if (horizontalFirst && (from == 'A' || from == '0')) {
                    horizontalFirst = false;
                } else if (!horizontalFirst && "147".contains(""+ from)) {
                    horizontalFirst = true;
                }
            } else {
                if (horizontalFirst && (from == 'A' || from == '^')) {
                    horizontalFirst = false;
                } else if (!horizontalFirst && from == '<') {
                    horizontalFirst = true;
                }
            }
            return horizontalFirst;
        }

        private final Map<Quad<Character, Character, Boolean, Boolean>, String> directionMemo = new HashMap<>();
        private String getDirections(char from, char to, boolean isNumpad, boolean horizontalFirst) {
            if (from == to)
                return "A";

            var quad = new Quad<>(from, to, isNumpad, horizontalFirst);
            var memo = directionMemo.get(quad);
            if (memo != null)
                return memo;

            var distance = getDistance(isNumpad ? NUMPAD : D_PAD, from, to);
            var vertical = distance.y() < 0 ? "^" : "v";
            var horizontal = distance.x() < 0 ? "<" : ">";

            var directions = horizontalFirst ?
                    buildDirectionalString(horizontal, vertical, distance.x(), distance.y()) :
                    buildDirectionalString(vertical, horizontal, distance.y(), distance.x());

            directions = directions + "A";
            directionMemo.put(quad, directions);
            return directions;
        }

        private static String buildDirectionalString(String first, String second, int d1, int d2) {
            return "%s%s".formatted(first.repeat(Math.abs(d1)), second.repeat(Math.abs(d2)));
        }

        private Coordinate getDistance(char[] keypad, char from, char to) {
            var c1 = positionOf(from, keypad);
            var c2 = positionOf(to, keypad);
            assert c1 != null;
            assert c2 != null;
            return new Coordinate(c2.y() - c1.y(), c2.x() - c1.x());
        }


        private Coordinate positionOf(char ch, char[] keypad) {

            for (int i = 0; i < keypad.length; i++) {
                if (keypad[i] == ch) {
                    var position = new Coordinate(
                            i / 3, i % 3
                    );
                    return position;
                }
            }

            return null;
        }

        private static final char[] NUMPAD = {
                '7', '8', '9',
                '4', '5', '6',
                '1', '2', '3',
                ' ', '0', 'A'
        };
        private static final char[] D_PAD = {
                ' ', '^', 'A',
                '<', 'v', '>'
        };
    }
}
