package aoc.aoc.days.implementation;

import aoc.aoc.days.enums.Part;
import aoc.aoc.days.interfaces.DayStringParser;
import aoc.aoc.util.GenericMatrix;
import aoc.aoc.util.Operand;

import java.util.*;
import java.util.stream.Collectors;

import static aoc.aoc.days.enums.Part.*;

public class Day06 extends DayStringParser {

    @Override
    protected Long part1Impl(String input) {
        return solveMathHomework(input);
    }

    @Override
    protected Long part2Impl(String input) {
        return solveMathHomework(input);
    }

    private long solveMathHomework(String input) {
        var lines = input.lines().collect(Collectors.toCollection(ArrayList::new));
        return buildRegions(lines, this.part).stream()
                .mapToLong(Day06::calculateRegion)
                .sum();
    }

    private static long calculateRegion(Region region) {
        return region.numbers.rows()
                .map(Day06::parseRow)
                .reduce(0L, region::calculate);
    }

    private static long parseRow(Character[] row) {
        long current = 0L;
        for (char ch : row)
            if (ch != ' ')
                current = current * 10 + (ch - '0');
        return current;
    }

    private static ArrayList<Region> buildRegions(List<String> lines, Part part) {
        var operands = lines.removeLast();
        var regions = new ArrayList<Region>();

        int start = operands.length();
        int end = operands.length();
        while (--start >= 0) {
            var ch = operands.charAt(start);
            if (!isOperand(ch))
                continue;

            regions.add(buildRegion(lines, start, end, part, ch));
            end = start - 1;
        }

        return regions;
    }

    private static Region buildRegion(
            List<String> lines, int start, int end, Part part, char ch
    ) {
        var region = new ArrayList<List<Character>>();

        for (var line : lines) {
            var row = new ArrayList<Character>();
            for (int j = start; j < end; j++)
                row.add(line.charAt(j));
            region.add(row);
        }

        var matrix = new GenericMatrix<>(region);
        matrix = part == PART_1 ? matrix : matrix.rotate90AntiClockwise();
        return new Region(matrix, Operand.fromChar(ch));
    }

    private static boolean isOperand(char ch) {
        return ch == '*' || ch == '+';
    }

    private record Region(GenericMatrix<Character> numbers, Operand operand) {
        long calculate(long a, long b) {
            return switch (operand) {
                case ADD -> a + b;
                case MUL -> Math.max(1L, a) * b;
                default -> throw new IllegalStateException("Unexpected value: " + operand);
            };
        }
    }
}
