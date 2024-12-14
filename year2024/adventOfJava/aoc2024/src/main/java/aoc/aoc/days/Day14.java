package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;
import aoc.aoc.util.Coordinate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static aoc.aoc.days.Part.PART_2;

public class Day14 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + new Space(parseRobots(input), 101, 103)
                .runForXSecond(100)
                .countSafetyFactor();
    }

    @Override
    protected String part2Impl(String input) {
        return "" + new Space(parseRobots(input), 101, 103)
                .with(PART_2)
                .runForXSecond(10000)
                .secondsToFindXmasTree();
    }

    private static List<Robot> parseRobots(String input) {
        return input.lines().map(line -> {
            List<Integer> numbers = parseNumbers(line);
            return new Robot(
                    new Coordinate(numbers.get(1), numbers.get(0)),
                    new Coordinate(numbers.get(3), numbers.get(2))
            );
        }).toList();
    }

    private static List<Integer> parseNumbers(String line) {
        List<Integer> numbers = new ArrayList<>(4);

        var halves = line.split(" ");
        for (var half : halves) {
            var parts = half.split(",");
            var first = parts[0].split("=")[1];
            numbers.add(Integer.parseInt(first));
            numbers.add(Integer.parseInt(parts[1]));
        }

        return numbers;
    }

    @RequiredArgsConstructor
    private static class Space extends AbstractSolver<Space> {

        private static final List<String> XMAS_TOP = List.of(
                "...#...",
                "..###..",
                ".#####.",
                "#######"
        );

        private final List<Robot> robots;
        private final int width;
        private final int height;
        @Getter
        @Accessors(fluent = true)
        private int secondsToFindXmasTree = -1;

        public Space runForXSecond(int seconds) {
            for (int i = 0; i < seconds; i++) {
                moveRobots();
                if (part == PART_2 && robots.size() >= 100) {
                    int took = lookForXmasTree(i);
                    if (took > 0) {
                        secondsToFindXmasTree = took;
                        return this;
                    }
                }
            }

            return this;
        }

        private int lookForXmasTree(int seconds) {
            var str = spaceToString();
            var lines = str.lines().toList();
            int streak = 0;
            for (var line : lines) {
                if (streak >= XMAS_TOP.size())
                    return seconds + 1;

                if (line.contains(XMAS_TOP.get(streak)))
                    streak++;
            }

            return -1;
        }

        public int countSafetyFactor() {
            int midY = height / 2;
            int midX = width / 2;

            List<Integer> quadrantSafetyFactors = List.of(
                    countQuadrantSafetyFactor(new Coordinate(0, 0), new Coordinate(midY, midX)),
                    countQuadrantSafetyFactor(new Coordinate(0, midX + 1), new Coordinate(midY, width)),
                    countQuadrantSafetyFactor(new Coordinate(midY + 1, 0), new Coordinate(height, midX)),
                    countQuadrantSafetyFactor(new Coordinate(midY + 1, midX + 1), new Coordinate(height, width))
            );

            return quadrantSafetyFactors.stream()
                    .reduce((a, b) -> a * b)
                    .orElse(-1);
        }

        private int countQuadrantSafetyFactor(Coordinate start, Coordinate end) {
            var atomic = new AtomicInteger();
            robots.forEach(robot -> {
                int y = robot.position().y();
                int x = robot.position().x();

                if (y >= start.y() && y < end.y() && x >= start.x() && x < end.x())
                    atomic.incrementAndGet();
            });

            return atomic.get();
        }

        private void moveRobots() {
            robots.forEach(robot -> {
                var position = robot.position();
                var velocity = robot.velocity();

                var nextPosition = new Coordinate(
                        wrapNumber(position.y() + velocity.y(), height),
                        wrapNumber(position.x() + velocity.x(), width)
                );

                robot.position(nextPosition);
            });
        }

        private int wrapNumber(int number, int boundary) {
            return number < 0 ? number + boundary : number % boundary;
        }

        private String spaceToString() {
            var sbList = new ArrayList<StringBuilder>();

            for (int i = 0; i < height; i++) {
                var sb = new StringBuilder();
                sb.append(".".repeat(Math.max(0, width)));
                sb.append("%n".formatted());
                sbList.add(sb);
            }

            robots.forEach(robot -> sbList.get(robot.position.y()).setCharAt(robot.position().x(), '#'));
            return sbList.stream()
                    .map(StringBuilder::toString)
                    .collect(Collectors.joining());
        }
    }

    @Accessors(fluent = true)
    @Getter
    private static class Robot {

        @Setter
        private Coordinate position;
        private final Coordinate velocity;

        public Robot(Coordinate position, Coordinate velocity) {
            this.position = position;
            this.velocity = velocity;
        }
    }
}
