package aoc.aoc.days.implementation;

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
        return "";
    }

    private record Rule(String w1, BinaryOperator<Boolean> gate, String w2, String resultingWire) {
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
        for (var e : results.entrySet()) {
            if (e.getKey().charAt(0) != 'z')
                break;

            var b = e.getValue();
            bigint <<= 1;
            if (Boolean.TRUE.equals(b))
                bigint++;
        }

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
