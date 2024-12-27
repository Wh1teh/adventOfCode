package aoc.aoc.days.implementation;

import aoc.aoc.util.Pair;
import lombok.AllArgsConstructor;

import java.util.*;

public class Day01 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        var numbers = getNumbers(input.lines().toList());
        int distances = sumNumbers(getDistances(numbers.first(), numbers.second()));

        return "" + distances;
    }

    @Override
    protected String part2Impl(String input) {
        var numbers = getNumbers(input.lines().toList());
        var similarityScores = getSimilarityScores(numbers.first().stream().toList(), numbers.second().stream().toList());
        int sum = sumSimilarityScores(similarityScores);

        return "" + sum;
    }

    private static Pair<PriorityQueue<Integer>, PriorityQueue<Integer>> getNumbers(
            List<String> lines) {

        PriorityQueue<Integer> left = new PriorityQueue<>();
        PriorityQueue<Integer> right = new PriorityQueue<>();

        lines.forEach(line -> {
                    if (line.isBlank()) {
                        return;
                    }

                    var numbers = line.split("\\s+");
                    left.add(Integer.parseInt(numbers[0]));
                    right.add(Integer.parseInt(numbers[1]));
                }
        );

        return new Pair<>(left, right);
    }

    private static List<Integer> getDistances(PriorityQueue<Integer> left, PriorityQueue<Integer> right) {
        List<Integer> distances = new ArrayList<>();

        assert left != right;
        while (left.peek() != null && right.peek() != null) {
            int distance = Math.abs(left.poll() - right.poll());
            distances.add(distance);
        }

        return distances;
    }

    private static int sumNumbers(List<Integer> numbers) {
        return numbers.stream().mapToInt(Integer::intValue).sum();
    }

    private static Map<Integer, SimilarityScore> getSimilarityScores(List<Integer> left, List<Integer> right) {
        Map<Integer, SimilarityScore> similarityScores = new HashMap<>();

        for (var n : left) {
            similarityScores.compute(n, (leftNumber, similarityScore) -> {
                if (similarityScore == null) {
                    similarityScore = new SimilarityScore(leftNumber);

                    for (var rightNumber : right) {
                        if (rightNumber.equals(leftNumber)) {
                            similarityScore.appearancesOnRight++;
                        }
                    }
                } else {
                    similarityScore.appearancesOnLeft++;
                }

                return similarityScore;
            });
        }

        return similarityScores;
    }

    private static int sumSimilarityScores(Map<Integer, SimilarityScore> similarityScores) {
        int sum = 0;

        for (var a : similarityScores.entrySet()) {
            sum += a.getValue().calculateTotalScore();
        }

        return sum;
    }

    @AllArgsConstructor
    private static class SimilarityScore {

        protected final int baseNumber;
        protected int appearancesOnLeft = 1;
        protected int appearancesOnRight = 0;

        protected SimilarityScore(int baseNumber) {
            this.baseNumber = baseNumber;
        }

        protected int calculateTotalScore() {
            return baseNumber * appearancesOnRight * appearancesOnLeft;
        }
    }
}
