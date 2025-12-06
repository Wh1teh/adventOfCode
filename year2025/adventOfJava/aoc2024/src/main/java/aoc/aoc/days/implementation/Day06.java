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
    protected String part1Impl(String input) {
        return solveMathHomework(input);
    }

    @Override
    protected String part2Impl(String input) {
        return solveMathHomework(input);
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

    private String solveMathHomework(String input) {
        var lines = input.lines().collect(Collectors.toCollection(ArrayList::new));
        long result = 0L;
        for (var region : buildRegions(lines, this.part))
            result += calculateRegion(region);

        return "" + result;
    }

    private static long calculateRegion(Region region) {
        var matrix = region.numbers;

        long total = 0L;
        for (int y = 0; y < matrix.height(); y++) {
            long current = 0L;
            for (int x = 0; x < matrix.width(); x++) {
                char ch = matrix.get(y, x);
                if (ch != ' ')
                    current = current * 10 + (ch - '0');
            }
            
            total = region.calculate(total, current);
        }

        return total;
    }

    private static ArrayList<Region> buildRegions(ArrayList<String> lines, Part part) {
        var operands = lines.removeLast();
        var regions = new ArrayList<Region>();

        int start = operands.length();
        int end = operands.length();
        while (--start >= 0) {
            var ch = operands.charAt(start);
            if (!isOperand(ch))
                continue;

            regions.add(new Region(
                    buildRegionMatrix(lines, start, end, part), Operand.fromChar(ch)
            ));

            end = start - 1;
        }

        return regions;
    }

    private static GenericMatrix<Character> buildRegionMatrix(
            List<String> lines, int start, int end, Part part
    ) {
        var region = new ArrayList<List<Character>>();

        for (var line : lines) {
            var row = new ArrayList<Character>();
            for (int j = start; j < end; j++)
                row.add(line.charAt(j));
            region.add(row);
        }

        var matrix = new GenericMatrix<>(region);
        return part == PART_1 ? matrix : matrix.rotate90AntiClockwise();
    }

    private static boolean isOperand(char ch) {
        return ch == '*' || ch == '+';
    }
}
