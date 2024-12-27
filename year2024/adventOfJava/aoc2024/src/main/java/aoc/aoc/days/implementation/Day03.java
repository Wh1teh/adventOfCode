package aoc.aoc.days.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + calculateMuls(getNumberPairsWithRegex(input));
    }

    @Override
    protected String part2Impl(String input) {
        return "" + calculateMuls(getAcceptableMuls(input));
    }

    private List<int[]> getNumberPairsWithRegex(String input) {
        Pattern pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
        Matcher matcher = pattern.matcher(input);

        List<int[]> numbers = new ArrayList<>();

        while (matcher.find()) {
            int num1 = Integer.parseInt(matcher.group(1));
            int num2 = Integer.parseInt(matcher.group(2));
            numbers.add(new int[]{num1, num2});
        }

        return numbers;
    }

    private int calculateMuls(List<int[]> numberPairs) {
        return numberPairs.stream()
                .mapToInt(pair -> pair[0] * pair[1])
                .sum();
    }

    private List<int[]> getAcceptableMuls(String input) {
        String[] doOrDonts = input.split("(?=do\\(\\)|don't\\(\\))|(?<=do\\(\\)|don't\\(\\))");
        List<int[]> numberPairs = new ArrayList<>();

        boolean canDo = true;
        for (var stretch : doOrDonts) {
            if (stretch.equals("do()"))
                canDo = true;
            else if (stretch.equals("don't()"))
                canDo = false;
            else if (canDo)
                numberPairs.addAll(getNumberPairsWithRegex(stretch));
        }

        return numberPairs;
    }
}
