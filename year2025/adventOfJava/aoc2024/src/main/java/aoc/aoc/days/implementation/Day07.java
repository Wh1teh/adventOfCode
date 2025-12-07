package aoc.aoc.days.implementation;

import aoc.aoc.cache.Memoize;
import aoc.aoc.days.interfaces.DayStringParser;
import aoc.aoc.util.Coordinate;
import aoc.aoc.util.GenericMatrix;

import java.util.Set;

public class Day07 extends DayStringParser {

    @Override
    protected String part1Impl(String input) {
        var set = goDownUntilNode(GenericMatrix.charMatrix(input), 0, input.indexOf('S'));
        return "" + set.size();
    }

    @Override
    protected String part2Impl(String input) {
        return "" + (goDownUntilNode2(GenericMatrix.charMatrix(input), 0, input.indexOf('S')) + 1);
    }

    @Memoize
    protected long goDownUntilNode2(GenericMatrix<Character> matrix, int y, int x) {
        long res = 0;
        try {
            while (matrix.get(y, x) != '^')
                ++y;

            ++res;
            res += (goDownUntilNode2(matrix, y, x - 1));
            res += (goDownUntilNode2(matrix, y, x + 1));
        } catch (Exception e) {
            // don't care
        }

        return res;
    }

    @Memoize
    protected Set<Coordinate> goDownUntilNode(GenericMatrix<Character> matrix, int y, int x) {
        var set = new java.util.HashSet<Coordinate>();
        try {
            while (matrix.get(y, x) != '^')
                ++y;

            set.add(new Coordinate(y, x));
            set.addAll(goDownUntilNode(matrix, y, x - 1));
            set.addAll(goDownUntilNode(matrix, y, x + 1));
        } catch (Exception e) {
            // don't care
        }

        return set;
    }
}
