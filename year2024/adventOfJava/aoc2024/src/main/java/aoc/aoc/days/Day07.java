package aoc.aoc.days;

import java.util.*;

public class Day07 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + solve(input, false);
    }

    @Override
    protected String part2Impl(String input) {
        return "" + solve(input, true);
    }

    private static long solve(String input, boolean part2) {
        long sum = 0;

        var calibrations = input.lines().map(Calibration::new).toList();
        for (var calibration : calibrations) {
            if (isPossible(calibration, part2))
                sum += calibration.result;
        }

        return sum;
    }

    private static boolean isPossible(Calibration calibration, boolean part2) {
        var numbers = calibration.numbers;
        for (int iteration = 1; iteration < numbers.size(); iteration++) {
            if (isPossibleRecursive(
                    numbers.reversed().stream().collect(Stack::new, Stack::push, Stack::addAll),
                    calibration.result,
                    part2
            )) return true;
        }

        return false;
    }

    private static boolean isPossibleRecursive(Stack<Long> numbers, long target, boolean part2) {
        if (numbers.size() == 1) {
            return numbers.getFirst() == target;
        } else
            return attemptOperators(numbers,target,part2);
    }

    private static boolean attemptOperators(Stack<Long> numbers, long target, boolean part2) {
        for (int i = 0; i < (part2 ? 3 : 2); i++) {
            var a = numbers.pop();
            var b = numbers.pop();

            numbers.push(operation(a, b, Operator.values()[i]));
            if (isPossibleRecursive(numbers, target, part2))
                return true;

            restoreStack(numbers, b, a);
        }

        return false;
    }

    private static void restoreStack(Stack<Long> numbers, Long b, Long a) {
        numbers.pop();
        numbers.push(b);
        numbers.push(a);
    }

    enum Operator {
        ADD,
        MUL,
        CONCAT
    }

    private static long operation(long a, long b, Operator operator) {
        return switch (operator) {
            case ADD -> a + b;
            case MUL -> a * b;
            case CONCAT -> a * (long) Math.pow(10, (int) Math.log10(b) + 1d) + b;
        };
    }

    private static class Calibration {

        public final long result;
        public final List<Long> numbers;

        public Calibration(String line) {
            String[] split = line.split(":");
            this.result = Long.parseLong(split[0]);

            this.numbers = new ArrayList<>(
                    Arrays.stream(split[1].trim().split(" "))
                            .map(Long::parseLong).toList()
            );
        }
    }

}
