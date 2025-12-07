package aoc.aoc.days.implementation;

import aoc.aoc.cache.Memoize;
import aoc.aoc.days.interfaces.DayStringParser;
import aoc.aoc.util.Coordinate;
import aoc.aoc.util.GenericMatrix;
import aoc.aoc.util.Matrix;

import java.util.Set;

import static aoc.aoc.util.GenericMatrix.charMatrix;
import static aoc.aoc.util.MatrixUtils.*;

public class Day07 extends DayStringParser {
    
    @Override
    protected String part1Impl(String input) {
        var set = p1(charMatrix(input), 0, input.indexOf('S'));
        return "" + set.size();
    }

    @Override
    protected String part2Impl(String input) {
        return "" + (p2(charMatrix(input), 0, input.indexOf('S')) + 1);
    }
    
    @Memoize
    protected long p2(Matrix<Character> matrix, int y, int x) {
        if (notWithinMatrix(y, x, matrix))
            return 0L;

        while (notEndOrSplitter(matrix, y, x))
            ++y;

        return y == matrix.height() ? 0L 
                : 1L 
                + p2(matrix, y, x - 1) 
                + p2(matrix, y, x + 1);
    }

    @Memoize
    protected Set<Coordinate> p1(GenericMatrix<Character> matrix, int y, int x) {
        var set = new java.util.HashSet<Coordinate>();
        try {
            while (matrix.get(y, x) != '^')
                ++y;

            set.add(new Coordinate(y, x));
            set.addAll(p1(matrix, y, x - 1));
            set.addAll(p1(matrix, y, x + 1));
        } catch (Exception e) {
            // don't care
        }

        return set;
    }

    private static boolean notEndOrSplitter(Matrix<Character> matrix, int y, int x) {
        return y < matrix.height() && matrix.get(y, x) != '^';
    }
}
