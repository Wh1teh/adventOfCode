package aoc.aoc.days.implementation;

import aoc.aoc.solver.AbstractSolver;
import aoc.aoc.util.Utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntUnaryOperator;

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
        public static final int LEAST_5_BITS = 0b11111;
        public static final int LEAST_20_BITS = 0xfffff;
        private final List<Integer> secrets;

        public SecretNumberCounter(String input) {
            this.secrets = input.lines().map(Integer::parseInt).toList();
        }

        public List<Integer> getNthSecretForEach() {
            return secrets.stream().map(secret -> {
                for (int i = 0; i < ROUNDS; i++) {
                    secret = doAllOperations(secret);
                }
                return secret;
            }).toList();
        }

        @SuppressWarnings("java:S117")
        public int getMostBananasPossible() {
            var differences = accumulateBananasForSequences(secrets);
            return getLargestAccumulation(differences);
        }

        private static Map<Integer, Integer> accumulateBananasForSequences(List<Integer> secrets) {
            Map<Integer, Integer> differences = new ConcurrentHashMap<>();

            Utils.forEachWithExecutorService(secrets,
                    secret -> accumulateBananaSequencesForSecret(secret, differences)
            );

            return differences;
        }

        private static void accumulateBananaSequencesForSecret(Integer secret, Map<Integer, Integer> differences) {
            Set<Integer> encountered = new HashSet<>();
            int sequence = 0;

            int previous = -1 * lastDigit(secret);
            for (int i = 0; i < ROUNDS; i++) {
                secret = doAllOperations(secret);
                int lastDigit = lastDigit(secret);
                byte differenceToPrevious = (byte) (previous - lastDigit);
                previous = lastDigit;

                sequence = queueDifferenceToSequence(sequence, differenceToPrevious);
                if (!encountered.contains(sequence)) {
                    encountered.add(sequence);

                    differences.merge(sequence, lastDigit, Integer::sum);
                }
            }
        }

        private static int queueDifferenceToSequence(int sequence, byte differenceToPrevious) {
            return ((sequence << 5) | (differenceToPrevious & LEAST_5_BITS)) & LEAST_20_BITS;
        }

        private static int getLargestAccumulation(Map<?, Integer> sequences) {
            int result = -1;

            for (var accumulation : sequences.values()) {
                result = Math.max(result, accumulation);
            }

            return result;
        }

        private static int lastDigit(int secret) {
            return secret % 10;
        }

        private static int doAllOperations(int secret) {
            secret = doOperation(secret, s -> s << 6);
            secret = doOperation(secret, s -> s >> 5);
            return doOperation(secret, s -> s << 11);
        }

        private static int doOperation(int secret, IntUnaryOperator operation) {
            int value = operation.applyAsInt(secret);
            secret = mix(value, secret);
            return prune(secret);
        }

        private static int mix(int value, int secret) {
            return value ^ secret;
        }

        private static int prune(int secret) {
            return secret & 0xffffff;
        }
    }
}
