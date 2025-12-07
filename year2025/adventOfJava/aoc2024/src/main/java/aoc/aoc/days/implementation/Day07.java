package aoc.aoc.days.implementation;

import aoc.aoc.cache.MemoIgnore;
import aoc.aoc.cache.Memoize;
import aoc.aoc.days.interfaces.DayStringParser;
import aoc.aoc.util.Coordinate;
import aoc.aoc.util.Matrix;

import java.util.HashSet;

import static aoc.aoc.util.GenericMatrix.charMatrix;
import static aoc.aoc.util.MatrixUtils.*;
import static aoc.aoc.util.Utils.Collections.addAndReturn;

public class Day07 extends DayStringParser {

    public static final char START = 'S';
    public static final char SPLITTER = '^';

    @Override
    protected String part1Impl(String input) {
        return "" + shootBeam(input, new HashSet<>(),
                (coordinate, set, __) -> addAndReturn(set, coordinate))
                .size();
    }

    @Override
    protected String part2Impl(String input) {
        return "" + shootBeam(input, 1L, (__, left, right) -> left + right);
    }

    @FunctionalInterface
    protected interface TraversalFunction<T> {
        T accumulate(Coordinate coordinate, T left, T right);
    }

    private <T> T shootBeam(String input, T acc, TraversalFunction<T> action) {
        return traverseDown(0, input.indexOf(START), charMatrix(input), acc, action);
    }

    @Memoize
    protected <T> T traverseDown(
            int y, int x, Matrix<Character> matrix,
            @MemoIgnore T acc, @MemoIgnore TraversalFunction<T> fn
    ) {
        if (notWithinMatrix(y, x, matrix))
            return acc;

        while (notEndOrSplitter(y, x, matrix))
            ++y;

        return isEnd(y, matrix) ? acc
                : fn.accumulate(new Coordinate(y, x),
                traverseDown(y, x - 1, matrix, acc, fn),
                traverseDown(y, x + 1, matrix, acc, fn)
        );
    }

    private static boolean isEnd(int y, Matrix<Character> matrix) {
        return y == matrix.height();
    }

    private static boolean notEndOrSplitter(int y, int x, Matrix<Character> matrix) {
        return y < matrix.height() && matrix.get(y, x) != SPLITTER;
    }
}
