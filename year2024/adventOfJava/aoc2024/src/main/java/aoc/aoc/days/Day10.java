package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;
import aoc.aoc.util.Coordinate;
import aoc.aoc.util.StringMatrix;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static aoc.aoc.days.Part.PART_1;
import static aoc.aoc.days.Part.PART_2;
import static aoc.aoc.util.StringMatrix.matrix;

public class Day10 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + new TrailCounter(matrix(input))
                .with(PART_1)
                .calculateTrailScores();
    }

    @Override
    protected String part2Impl(String input) {
        return "" + new TrailCounter(matrix(input))
                .with(PART_2)
                .calculateTrailScores();
    }

    @RequiredArgsConstructor
    private static class TrailCounter extends AbstractSolver<TrailCounter> {

        public static final char TRAIL_START = '0';
        public static final char TRAIL_END = '9';

        private final StringMatrix matrix;

        public int calculateTrailScores() {
            AtomicInteger trailScores = new AtomicInteger();

            matrix.iterate((ch, y, x) -> {
                if (ch == TRAIL_START) {
                    trailScores.getAndAdd(switch (part) {
                        case PART_1 -> countTrailEnds(new Coordinate(y, x));
                        case PART_2 -> countTrailPaths(new Coordinate(y, x));
                    });
                }
            });

            return trailScores.get();
        }

        private int countTrailPaths(Coordinate posisition) {
            return countTrailPaths(posisition, new AtomicInteger(0));
        }

        private int countTrailPaths(Coordinate position, AtomicInteger score) {
            char currentNumber = matrix.get(position);
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
            char currentNumber = matrix.get(position);
            applyAdjacent(
                    position,
                    nextNumber -> currentNumber - nextNumber == -1,
                    nextPosition -> {
                        if (matrix.get(nextPosition) == TRAIL_END)
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
            if (y < 0 || x < 0 || y >= matrix.width() || x >= matrix.width())
                return;

            char ch = matrix.get(y, x);
            if (predicate.test(ch))
                action.accept(new Coordinate(y, x));
        }
    }
}
