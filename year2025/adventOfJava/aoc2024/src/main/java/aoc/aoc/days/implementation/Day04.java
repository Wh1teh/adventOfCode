package aoc.aoc.days.implementation;

import aoc.aoc.util.*;

public class Day04 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        var matrix = GenericMatrix.charMatrix(input);
        int[] validRolls = {0};

        matrix.iterate((ch, coordinate) -> {
            if (ch != '@')
                return;

            if (countRollsAround(coordinate, matrix) < 4)
                ++validRolls[0];
        });

        return "" + validRolls[0];
    }

    @Override
    protected String part2Impl(String input) {
        var matrix = GenericMatrix.charMatrix(input);
        int[] validRolls = {0};

        boolean[] wasRemovedLastRound = {true};
        while (wasRemovedLastRound[0]) {
            wasRemovedLastRound[0] = false;

            matrix.iterate((ch, coordinate) -> {
                if (ch != '@')
                    return;

                if (countRollsAround(coordinate, matrix) < 4) {
                    wasRemovedLastRound[0] = true;
                    matrix.set(coordinate, 'x');
                    ++validRolls[0];
                }
            });
        }

        return "" + validRolls[0];
    }

    private static int countRollsAround(Coordinate coordinate, Matrix<Character> matrix) {
        int[] adjacent = {0};
        MatrixUtils.applyAround(
                matrix, coordinate, c -> c == '@', __ -> ++adjacent[0]
        );
        return adjacent[0];
    }
}
