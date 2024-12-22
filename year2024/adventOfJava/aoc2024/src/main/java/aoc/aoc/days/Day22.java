package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;

import java.util.*;
import java.util.function.LongUnaryOperator;

public class Day22 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + new SecretNumberCounter(input)
                .getNthSecretForEach()
                .stream().mapToLong(Long::valueOf).sum();
    }

    @Override
    protected String part2Impl(String input) {
        if (isSample)
            input = """
                    1
                    2
                    3
                    2024""";

        return "" + new SecretNumberCounter(input)
                .getMostBananasPossible();
    }

    private static class SecretNumberCounter extends AbstractSolver<SecretNumberCounter> {

        private static final int ROUNDS = 2000;
        private final List<Long> secrets;

        public SecretNumberCounter(String input) {
            this.secrets = input.lines().map(Long::parseLong).toList();
        }

        public List<Long> getNthSecretForEach() {
            return secrets.stream().map(secret -> {
                for (int i = 0; i < ROUNDS; i++) {
                    secret = doAllOperations(secret);
                }
                return secret;
            }).toList();
        }

        @SuppressWarnings("java:S117")
        public long getMostBananasPossible() {
            var differences = accumulateBananasForSequences(secrets);
            return getLargestAccumulation(differences);
        }

        private static Map<List<Long>, Long> accumulateBananasForSequences(List<Long> secrets) {
            Map<List<Long>, Long> differences = new HashMap<>();

            secrets.forEach(secret -> {
                Set<List<Long>> encountered = new HashSet<>();
                Deque<Long> diffs = new ArrayDeque<>();

                long previous = -1 * lastDigit(secret);
                for (int i = 0; i < ROUNDS; i++) {
                    secret = doAllOperations(secret);
                    long lastDigit = lastDigit(secret);
                    long differenceToPrevious = previous - lastDigit;
                    previous = lastDigit;

                    if (diffs.size() >= 4)
                        diffs.pop();
                    diffs.add(differenceToPrevious);

                    var sequence = diffs.stream().toList();
                    if (!encountered.contains(sequence)) {
                        encountered.add(sequence);

                        differences.merge(sequence, lastDigit, Long::sum);
                    }
                }
            });

            return differences;
        }

        private static long getLargestAccumulation(Map<List<Long>, Long> sequences) {
            long result = -1L;

            for (var accumulation : sequences.values()) {
                result = Math.max(result, accumulation);
            }

            return result;
        }

        private static long lastDigit(long secret) {
            return secret % 10;
        }

        private static long doAllOperations(long secret) {
            secret = doOperation(secret, s -> s << 6);
            secret = doOperation(secret, s -> s >> 5);
            return doOperation(secret, s -> s << 11);
        }

        private static long doOperation(long secret, LongUnaryOperator operation) {
            long value = operation.applyAsLong(secret);
            secret = mix(value, secret);
            return prune(secret);
        }

        private static long mix(long value, long secret) {
            return value ^ secret;
        }

        private static long prune(long secret) {
            return secret & 0xffffff;
        }
    }
}
