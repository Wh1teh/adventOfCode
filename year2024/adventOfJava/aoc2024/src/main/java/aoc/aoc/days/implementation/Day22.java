package aoc.aoc.days.implementation;

import aoc.aoc.util.Utils;

import java.util.*;
import java.util.function.IntUnaryOperator;

public class Day22 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + getNthSecretForEach(input)
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

        return "" + getMostBananasPossible(input);
    }

    private static final int ROUNDS = 2000;
    private static final int LEAST_5_BITS = 0b11111;

    private static List<Integer> getNthSecretForEach(String input) {
        return parseSecrets(input).stream().map(secret -> {
            for (int i = 0; i < ROUNDS; i++) {
                secret = doAllOperations(secret);
            }
            return secret;
        }).toList();
    }

    @SuppressWarnings("java:S117")
    public static int getMostBananasPossible(String input) {
        var sequences = accumulateBananasForSequences(parseSecrets(input));
        return Arrays.stream(sequences).max().orElse(-1);
    }

    private static int[] accumulateBananasForSequences(List<Integer> secrets) {
        int[] sequences = new int[19 * 19 * 19 * 19];

        Utils.forEachWithExecutorService(secrets,
                secret -> accumulateBananaSequencesForSecret(secret, sequences)
        );

        return sequences;
    }

    /**
     * Decided to unroll a bunch of stuff because method calls slowed everything down by a lot,
     * and that is why this method exceptionally has comments
     */
    private static void accumulateBananaSequencesForSecret(int secret, int[] sequences) {
        int sequence = 0;
        // This wastes a lot of memory compared to a set, but it's also way faster
        boolean[] visited = new boolean[19 * 19 * 19 * 19];

        int previousDigit = secret % 10;
        for (int i = 0; i < ROUNDS; i++) {
            // this::doAllOperations unrolled
            secret = ((secret << 6) ^ secret) & 0xffffff;
            secret = ((secret >> 5) ^ secret) & 0xffffff;
            secret = ((secret << 11) ^ secret) & 0xffffff;

            int currentDigit = secret % 10;
            // Can store sequence in a queue of 20 bits
            sequence = (sequence >> 5) + ((currentDigit - previousDigit + 9) << 15);
            previousDigit = currentDigit;

            // add to result "map" if the local "set" doesn't contain sequence
            int a = sequence >> 15;
            int b = sequence >> 10 & LEAST_5_BITS;
            int c = sequence >> 5 & LEAST_5_BITS;
            int d = sequence & LEAST_5_BITS;
            int index = a * (19 * 19 * 19) + b * (19 * 19) + c * 19 + d;
            if (!visited[index]) {
                visited[index] = true;
                sequences[index] += currentDigit;
            }
        }
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

    private static List<Integer> parseSecrets(String input) {
        return input.lines().map(Integer::parseInt).toList();
    }
}
