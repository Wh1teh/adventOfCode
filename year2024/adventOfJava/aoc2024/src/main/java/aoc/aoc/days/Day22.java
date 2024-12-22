package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongUnaryOperator;

public class Day22 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        var a = new SecretNumberCounter(input)
                .with(Part.PART_1)
                .getNthSecretForEach(2000);
        return "" + a.stream().mapToLong(Long::valueOf).sum();
    }

    @Override
    protected String part2Impl(String input) {
        if (isSample)
            input = """
                    1
                    2
                    3
                    2024""";

        var a = new SecretNumberCounter(input)
                .with(Part.PART_1)
                .getNthSecretForEach2(2000);
        return "" + a;
    }

    private static class SecretNumberCounter extends AbstractSolver<SecretNumberCounter> {

        private final List<Long> secrets;

        public SecretNumberCounter(String input) {
            this.secrets = input.lines().map(Long::parseLong).toList();
        }

        public long getNthSecretForEach2(final long rounds) {
            List<Map<List<Long>, Long>> differences = new ArrayList<>();

            secrets.forEach(secret -> {
                Map<List<Long>, Long> m = new HashMap<>();
                Deque<Long> diffs = new ArrayDeque<>();
                var sb = new StringBuilder();

                long previous = difference(0, lastDigit(secret, sb));
                for (int i = 0; i < rounds; i++) {
                    secret = doAllOperations(secret);
                    long lastDigit = lastDigit(secret, sb);
                    long diff = difference(previous, lastDigit);
                    previous = lastDigit;

                    if (diffs.size() >= 4)
                        diffs.pop();
                    diffs.add(diff);
                    m.putIfAbsent(diffs.stream().toList(),lastDigit);
                }

                differences.add(m);
            });

            long mostBananasPossible = -1;
            for (long i = -9; i < 10; i++) {
                for (long j = -9; j < 10; j++) {
                    for (long k = -9; k < 10; k++) {
                        for (long m = -9; m < 10; m++) {
                            var sequence = List.of(i, j, k, m);
                            var accumulate = new AtomicLong();
                            differences.forEach(map ->
                                    map.computeIfPresent(sequence, (set, bananas) -> {
                                        accumulate.addAndGet(bananas);
                                        return bananas;
                                    })
                            );
                            mostBananasPossible = Math.max(accumulate.get(),mostBananasPossible);
                        }
                    }
                }
            }

            return mostBananasPossible;
        }

        private Long difference(long previous, long lastDigit) {
            return lastDigit - previous;
        }

        private static long lastDigit(long secret, StringBuilder sb) {
            sb.append(secret);
            long result = (long) sb.charAt(sb.length() - 1) - '0';
            sb.setLength(0);
            return result;
        }

        public List<Long> getNthSecretForEach(final long rounds) {
            return secrets.stream().map(secret -> {
                for (int i = 0; i < rounds; i++) {
                    secret = doAllOperations(secret);
                }
                return secret;
            }).toList();
        }

        private long doAllOperations(long secret) {
            secret = doOperation(secret,s -> s << 6);
            secret = doOperation(secret,s -> s >> 5);
            return doOperation(secret,s -> s << 11);
        }

        private long doOperation(long secret, LongUnaryOperator operation) {
            long value = operation.applyAsLong(secret);
            secret = mix(value, secret);
            return prune(secret);
        }

        private long mix(long value, long secret) {
            return value ^ secret;
        }

        private long prune(long secret) {
            return secret & 16777215;
        }
    }
}
