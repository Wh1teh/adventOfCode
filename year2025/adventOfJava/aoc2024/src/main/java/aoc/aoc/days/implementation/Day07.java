package aoc.aoc.days.implementation;

import aoc.aoc.cache.MemoIgnore;
import aoc.aoc.cache.Memoize;
import aoc.aoc.days.interfaces.DayStringParser;
import aoc.aoc.util.Coordinate;
import aoc.aoc.util.Matrix;

import java.util.HashSet;
import java.util.Set;

import static aoc.aoc.util.GenericMatrix.charMatrix;
import static aoc.aoc.util.MatrixUtils.*;

public class Day07 extends DayStringParser {
    
    @Override
    protected String part1Impl(String input) {
        var set = new HashSet<Coordinate>();
        p1(0, input.indexOf('S'),charMatrix(input),set);
        return "" + set.size();
    }

    @Override
    protected String part2Impl(String input) {
        return "" + (p2( 0, input.indexOf('S'), charMatrix(input)) + 1);
    }
    
    @Memoize
    protected long p2(int y, int x, Matrix<Character> matrix) {
        if (notWithinMatrix(y, x, matrix))
            return 0L;

        while (notEndOrSplitter(y, x, matrix))
            ++y;

        if (y == matrix.height())
            return 0L;
        
        return 1L 
                + p2( y, x - 1,matrix) 
                + p2( y, x + 1,matrix);
    }

    @Memoize
    protected void p1(int y, int x, Matrix<Character> matrix, @MemoIgnore Set<Coordinate> set) {
        if (notWithinMatrix(y, x, matrix))
            return;

        while (notEndOrSplitter(y, x, matrix))
            ++y;
        if (y == matrix.height())
            return;

        set.add(new Coordinate(y, x));
        p1(y, x - 1, matrix, set);
        p1(y, x + 1, matrix, set);
    }

    private static boolean notEndOrSplitter(int y, int x, Matrix<Character> matrix) {
        return y < matrix.height() && matrix.get(y, x) != '^';
    }
}
