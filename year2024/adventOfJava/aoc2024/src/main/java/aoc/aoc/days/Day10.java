package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;
import aoc.aoc.util.Coordinate;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static aoc.aoc.days.Part.PART_1;
import static aoc.aoc.days.Part.PART_2;

public class Day10 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + new TrailCounter(input.lines().toList())
                .with(PART_1)
                .calculateTrailScores();
    }

    @Override
    protected String part2Impl(String input) {
        return "" + new TrailCounter(input.lines().toList())
                .with(PART_2)
                .calculateTrailScores();
    }

    @RequiredArgsConstructor
    private static class TrailCounter extends AbstractSolver<TrailCounter> {

        public static final char TRAIL_START = '0';
        public static final char TRAIL_END = '9';

        private final List<String> matrix;

        public int calculateTrailScores() {
            int trailScores = 0;

            for (int y = 0; y < matrix.size(); y++) {
                for (int x = 0; x < matrix.size(); x++) {
                    if (matrix.get(y).charAt(x) == TRAIL_START) {
                        trailScores += switch (part) {
                            case PART_1 -> countTrailEnds(new Coordinate(y, x));
                            case PART_2 -> countTrailPaths(new Coordinate(y, x));
                        };
                    }

                }
            }

            return trailScores;
        }

        private int countTrailPaths(Coordinate posisition) {
            return countTrailPaths(posisition, new AtomicInteger(0));
        }

        private int countTrailPaths(Coordinate position, AtomicInteger score) {
            char currentNumber = matrix.get(position.y()).charAt(position.x());
            applyAdjacent(
                    position,
                    nextNumber -> {
                        if (currentNumber == TRAIL_END - 1 && nextNumber == TRAIL_END)
                            score.incrementAndGet();
                        else
                            return currentNumber - nextNumber == -1;
                        return false;
                    },
                    nextPosition -> countTrailPaths(nextPosition, score)
            );

            return score.get();
        }

        private int countTrailEnds(Coordinate position) {
            return countTrailEnds(position, new HashSet<>());
        }

        private int countTrailEnds(Coordinate position, Set<Coordinate> trailEnds) {
            char currentNumber = matrix.get(position.y()).charAt(position.x());
            applyAdjacent(
                    position,
                    nextNumber -> currentNumber - nextNumber == -1,
                    nextPosition -> {
                        if (matrix.get(nextPosition.y()).charAt(nextPosition.x()) == TRAIL_END)
                            trailEnds.add(nextPosition);
                        else
                            countTrailEnds(nextPosition, trailEnds);
                    }
            );

            return trailEnds.size();
        }

        private void applyAdjacent(Coordinate position, Predicate<Character> predicate, Consumer<Coordinate> action) {
            applyTo(position.y() - 1, position.x(), predicate, action);
            applyTo(position.y(), position.x() - 1, predicate, action);
            applyTo(position.y() + 1, position.x(), predicate, action);
            applyTo(position.y(), position.x() + 1, predicate, action);
        }

        private void applyTo(int y, int x, Predicate<Character> predicate, Consumer<Coordinate> action) {
            if (y < 0 || x < 0 || y >= matrix.size() || x >= matrix.size())
                return;

            char ch = matrix.get(y).charAt(x);
            if (predicate.test(ch))
                action.accept(new Coordinate(y, x));
        }
    }
}
