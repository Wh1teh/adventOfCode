package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Day24 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        var a = new GateHandler(input)
                .processGates();
        return "" + a;
    }

    @Override
    protected String part2Impl(String input) {
        return "";
    }

    private static class GateHandler extends AbstractSolver<GateHandler> {

        private final Map<String, Boolean> wires;
        private final Map<String, Boolean> results = new TreeMap<>(Comparator.reverseOrder());
        private final Deque<Rule> rules;

        record Rule(String w1, Gate gate, String w2, String resultingWire) {
        }

        public GateHandler(String input) {
            var parts = input.split("\\R{2}");
            this.wires = parseInitialWires(parts[0]);
            this.rules = parseRules(parts[1]);
        }

        private BigInteger processGates() {
            while (!rules.isEmpty()) {
                var rule = rules.poll();
                if (!processGate(rule))
                    rules.add(rule);
            }


            return parseBits(results);
        }

        private boolean processGate(Rule rule) {
            var w1 = wires.get(rule.w1);
            var w2 = wires.get(rule.w2);
            if (w1 == null || w2 == null)
                return false;

            wires.put(rule.resultingWire, runGate(rule.gate, w1, w2));
            results.put(rule.resultingWire, runGate(rule.gate, w1, w2));

            return true;
        }

        private boolean runGate(Gate gate, boolean a, boolean b) {
            return switch (gate) {
                case AND -> a && b;
                case OR -> a || b;
                case XOR -> a ^ b;
            };
        }

        private BigInteger parseBits(Map<String, Boolean> map) {
//            new TreeMap<>(map).forEach((k,v) -> System.out.println(k + ": " + (v.equals(Boolean.TRUE)? 1 :0)));
            var bigint = BigInteger.ZERO;
            var sb = new StringBuilder();
            for(var e : map.entrySet()) {
                if (!e.getKey().startsWith("z"))
                    continue;
                var b = e.getValue();
                bigint = bigint.shiftLeft(1);
                if (Boolean.TRUE.equals(b))
                     bigint = bigint.add(BigInteger.ONE);
                 else bigint = bigint.add(BigInteger.ZERO);

                 sb.append(Boolean.TRUE.equals(b) ? "1" : "0");
            }

            System.out.println(sb);
            return bigint;
//            return map.entrySet()
//                    .stream()
//                    .filter(entry -> entry.getKey().startsWith("z"))
//                    .collect(Collectors.toMap(
//                            Map.Entry::getKey,
//                            Map.Entry::getValue,
//                            (e1, e2) -> e1,
//                            TreeMap::new
//                    )).values().stream().map(b -> Boolean.TRUE.equals(b) ? BigInteger.ONE : BigInteger.ZERO)
//                    .reduce(BigInteger.ZERO,
//                            (result, n) -> result.shiftLeft(1).add(n)
//                    );
        }

        private Deque<Rule> parseRules(String input) {
            return input.lines().map(line -> {
                var parts = line.split(" ");

                var w1 = parts[0];
                var gate = Gate.of(parts[1].charAt(0));
                var w2 = parts[2];
                var resultingWire = parts[4];

                return new Rule(w1, gate, w2, resultingWire);
            }).collect(Collectors.toCollection(ArrayDeque::new));
        }

        private Map<String, Boolean> parseInitialWires(String input) {
            return input.lines()
                    .map(line -> line.split(": "))
                    .collect(Collectors.toMap(
                            parts -> parts[0], parts -> parts[1].equals("1"),
                            (existing, __) -> existing,
                            TreeMap::new
                    ));
        }

//        private static class Wire {
//
//            final String label;
//            boolean value;
//
//            Wire(String label, boolean value) {
//                this.label = label;
//                this.value = value;
//            }
//        }

        private enum Gate {
            AND,
            OR,
            XOR;

            static Gate of(char ch) {
                return switch (ch) {
                    case 'A' -> AND;
                    case 'O' -> OR;
                    case 'X' -> XOR;
                    default -> throw new IllegalStateException("Unexpected value: " + ch);
                };
            }
        }
    }
}
