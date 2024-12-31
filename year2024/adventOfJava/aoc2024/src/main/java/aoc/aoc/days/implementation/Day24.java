package aoc.aoc.days.implementation;

import aoc.aoc.util.Utils;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class Day24 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + processGates(input);
    }

    @Override
    protected String part2Impl(String input) {
        return isSample ? "[]" : analyzeGates(input);
    }

    private record Rule(String w1, BinaryOperator<Boolean> gate, String w2, String resultingWire) {
    }

    private record Gate(String left, String op, String right, String result) {
        @Override
        public String toString() {
            return "%s %s %s -> %s".formatted(left, op, right, result);
        }
    }

    /**
     * <a href="https://www.reddit.com/r/adventofcode/comments/1hla5ql/2024_day_24_part_2_a_guide_on_the_idea_behind_the/">I did not figure this out lol</a>
     */
    private String analyzeGates(String input) {
        var parts = input.split("\\R{2}");
        var gates = parseGates(parts[1]);

        var sus = gates.values().stream().filter(gate ->
                outputsZ_withoutXOR(gate)
                        || outputsZ_withXOR_withoutInputtingXY(gate)
                        || inputsXY_withXOR_withoutProperFollowUp(gate, gates)
                        || outputsNonZ_inputsNonXY_withAND_withoutProperFollowUp(gate, gates)
        ).map(g -> g.result).sorted().toList();

        assert sus.size() == 8;
        return Utils.toStringStripBracketsAndWhiteSpace(sus);
    }

    @SuppressWarnings("java:S100")
    private static boolean outputsNonZ_inputsNonXY_withAND_withoutProperFollowUp(
            Gate gate, Map<String, Gate> gates) {
        return gate.op.contains("AND") && inputsXY(gate) && notFirstXY(gate)
                && gates.values().stream().noneMatch(
                next -> next.op.equals("OR") && gateContainsPreviousResultAsInput(gate, next)
        );
    }

    @SuppressWarnings("java:S100")
    private static boolean inputsXY_withXOR_withoutProperFollowUp(Gate gate, Map<String, Gate> gates) {
        return isXOR(gate) && inputsXY(gate) && notFirstXY(gate)
                && gates.values().stream().noneMatch(
                next -> isXOR(next) && gateContainsPreviousResultAsInput(gate, next)
        );
    }

    @SuppressWarnings("java:S100")
    private static boolean outputsZ_withXOR_withoutInputtingXY(Gate gate) {
        return !outputsZ(gate) && !inputsXY(gate) && isXOR(gate);
    }

    @SuppressWarnings("java:S100")
    private static boolean outputsZ_withoutXOR(Gate gate) {
        return outputsZ(gate) && !isXOR(gate) && !gate.result.equals("z45");
    }

    private static boolean gateContainsPreviousResultAsInput(Gate previous, Gate current) {
        return current.left.equals(previous.result) || current.right.equals(previous.result);
    }

    private static boolean isXOR(Gate gate) {
        return gate.op.equals("XOR");
    }

    private static boolean notFirstXY(Gate gate) {
        return !gate.left.contains("00") || !gate.right.contains("00");
    }

    private static boolean inputsXY(Gate gate) {
        var l = gate.left.charAt(0);
        var r = gate.right.charAt(0);
        return !(l != 'x' && l != 'y') && !(r != 'x' && r != 'y');
    }

    private static boolean outputsZ(Gate gate) {
        return gate.result.charAt(0) == 'z';
    }

    private Map<String, Gate> parseGates(String input) {
        Map<String, Gate> gates = new HashMap<>();

        input.lines().forEach(line -> {
            var parts = line.split(" ");

            var w1 = parts[0];
            var gate = parts[1];
            var w2 = parts[2];
            var resultingWire = parts[4];

            assert !gates.containsKey(resultingWire);
            gates.put(resultingWire, new Gate(w1, gate, w2, resultingWire));
        });

        return gates;
    }

    private long processGates(String input) {
        Map<String, Boolean> results = new TreeMap<>(Comparator.reverseOrder());

        var parts = input.split("\\R{2}");
        var wires = parseInitialWires(parts[0]);
        var rules = parseRules(parts[1]);

        while (!rules.isEmpty()) {
            var rule = rules.poll();
            if (!processGate(rule, wires, results))
                rules.add(rule);
        }

        return parseBits(results);
    }

    private boolean processGate(Rule rule, Map<String, Boolean> wires, Map<String, Boolean> results) {
        var w1 = wires.get(rule.w1);
        var w2 = wires.get(rule.w2);
        if (w1 == null || w2 == null)
            return false;

        var result = rule.gate.apply(w1, w2);
        wires.put(rule.resultingWire, result);
        results.put(rule.resultingWire, result);

        return true;
    }

    private long parseBits(Map<String, Boolean> results) {
        long bigint = 0L;
        for (var e : results.entrySet())
            if (e.getKey().charAt(0) == 'z')
                bigint = (bigint << 1) + (Boolean.TRUE.equals(e.getValue()) ? 1L : 0L);

        return bigint;
    }

    private Deque<Rule> parseRules(String input) {
        return input.lines().map(line -> {
            var parts = line.split(" ");

            var w1 = parts[0];
            var gate = gateOf(parts[1].charAt(0));
            var w2 = parts[2];
            var resultingWire = parts[4];

            return new Rule(w1, gate, w2, resultingWire);
        }).collect(Collectors.toCollection(ArrayDeque::new));
    }

    private Map<String, Boolean> parseInitialWires(String input) {
        return input.lines()
                .map(line -> line.split(": "))
                .collect(Collectors.toMap(parts -> parts[0], parts -> parts[1].charAt(0) == '1'));
    }

    private BinaryOperator<Boolean> gateOf(char ch) {
        return switch (ch) {
            case 'A' -> Boolean::logicalAnd;
            case 'O' -> Boolean::logicalOr;
            case 'X' -> Boolean::logicalXor;
            default -> throw new IllegalStateException("Unexpected value: " + ch);
        };
    }
}
