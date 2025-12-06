package aoc.aoc.days.implementation;

import aoc.aoc.days.interfaces.DayStringParser;
import aoc.aoc.util.Direction;

public class Day01 extends DayStringParser {

    @Override
    protected String part1Impl(String input) {
        int current = 50;
        int counter = 0;
        for (var line : input.lines().toList()) {
            var r = parseClicks(line);

            current = rotate(current, r.direction, r.number) % 100;
            if (current == 0)
                ++counter;
        }

        return "" + counter;
    }

    @Override
    protected String part2Impl(String input) {
        int previous = 50;
        int counter = 0;
        for (var line : input.lines().toList()) {
            var r = parseClicks(line);

            int current = rotate(previous, r.direction(), r.number());
            counter += countPasses(previous, current);
            previous = Math.floorMod(current, 100);
        }

        return "" + counter;
    }

    private record Result(Direction direction, int number) {
    }

    private static Result parseClicks(String line) {
        var direction = Direction.getDirection(line.charAt(0));
        int number = Integer.parseInt(line.substring(1));
        return new Result(direction, number);
    }

    private static int rotate(int current, Direction direction, int clicks) {
        int directionalMultiplier = direction == Direction.LEFT ? -1 : 1;
        return (current + directionalMultiplier * clicks);
    }

    private static int countPasses(int previous, int current) {
        int c;
        if (current <= 0)
            c = (current - (previous == 0 ? 0 : 100)) / 100;
        else
            c = current / 100;

        return Math.abs(c);
    }
}
