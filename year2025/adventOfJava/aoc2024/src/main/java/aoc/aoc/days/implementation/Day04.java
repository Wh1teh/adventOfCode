package aoc.aoc.days.implementation;

import aoc.aoc.days.interfaces.DayStringParser;
import aoc.aoc.util.*;

import java.util.*;
import java.util.function.BiConsumer;

public class Day04 extends DayStringParser {

    @Override
    protected String part1Impl(String input) {
        var matrix = GenericMatrix.charMatrix(input);
        int[] validRolls = {0};

        matrix.iterate((ch, position) -> {
            if (isRoll(ch))
                return;

            if (countRollsAround(position, matrix) < 4)
                ++validRolls[0];
        });

        return "" + validRolls[0];
    }

    @Override
    protected String part2Impl(String input) {
        var matrix = GenericMatrix.charMatrix(input);
        int[] validRolls = {0};
        Deque<Coordinate> removedRolls = new ArrayDeque<>();

        BiConsumer<Character, Coordinate> removeRolls = (ch, position) -> {
            if (isRoll(ch))
                return;

            if (countRollsAround(position, matrix) < 4) {
                matrix.set(position, 'x');
                ++validRolls[0];
                removedRolls.add(position);
            }
        };

        matrix.iterate(removeRolls);
        while (!removedRolls.isEmpty())
            MatrixUtils.applyAround(matrix, removedRolls.pop(), removeRolls);

        return "" + validRolls[0];
    }

    private static boolean isRoll(Character ch) {
        return ch != '@';
    }

    private static int countRollsAround(Coordinate position, Matrix<Character> matrix) {
        int[] adjacent = {0};
        MatrixUtils.applyAround(
                matrix, position, c -> c == '@', __ -> ++adjacent[0]
        );
        return adjacent[0];
    }
}
