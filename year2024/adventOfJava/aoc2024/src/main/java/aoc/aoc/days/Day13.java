package aoc.aoc.days;

import java.math.BigInteger;
import java.util.*;

public class Day13 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + solve(input);
    }

    boolean part2 = false;

    @Override
    protected String part2Impl(String input) {
        part2 = true;
        return "" + solve(input);
    }

    private BigInteger solve(String input) {
        List<BigInteger> sum = new ArrayList<>();

        var prizeSets = getPrizeSets(input);
        prizeSets.forEach(prizeSet -> {
            var intersect = intersectOfTwoLines(prizeSet);
            if (isValidIntersect(prizeSet, intersect))
                sum.add(intersect.x().add(intersect.y().multiply(BigInteger.valueOf(3L))));
        });


        return sum.stream().reduce(BigInteger.ZERO, BigInteger::add);
    }

    private boolean isValidIntersect(PrizeSet prizeSet, Button<BigInteger> intersect) {
        var a = prizeSet.a();
        var b = prizeSet.b();
        var target = prizeSet.target();

        var axbx = a.x().multiply(intersect.y()).add(b.x().multiply(intersect.x()));
        var ayby = a.y().multiply(intersect.y()).add(b.y().multiply(intersect.x()));

        return axbx.equals(target.x()) && ayby.equals(target.y());
    }

    private Button<BigInteger> intersectOfTwoLines(PrizeSet prizeSet) {
        var a = prizeSet.a();
        var b = prizeSet.b();
        var c = prizeSet.target();

        var divisor = a.y().multiply(b.x()).subtract(a.x().multiply(b.y()));
        return new Button<>(
                b.x().multiply(c.y()).subtract(b.y().multiply(c.x())).divide(divisor),
                c.x().multiply(a.y()).subtract(c.y().multiply(a.x())).divide(divisor)
        );
    }

    private List<PrizeSet> getPrizeSets(String input) {
        var prizes = input.split("\\R{2}");
        return Arrays.stream(prizes).map(prize -> {
            var lines = prize.lines().toList();
            assert lines.size() == 3;
            var a = parseNumbersFromRight(lines.get(0));
            var b = parseNumbersFromRight(lines.get(1));
            var target = parseNumbersFromRight(lines.get(2));
            if (part2)
                target = new Button<>(
                        target.y().add(BigInteger.valueOf(10000000000000L)),
                        target.x().add(BigInteger.valueOf(10000000000000L))
                );
            return new PrizeSet(a, b, target);
        }).toList();
    }

    private Button<BigInteger> parseNumbersFromRight(String line) {
        var sb = new StringBuilder();

        BigInteger first = BigInteger.valueOf(0);
        BigInteger second = BigInteger.valueOf(0);
        boolean isFirstNumber = true;
        int i = line.length() - 1;
        while (i >= 0) {
            char ch = line.charAt(i);
            if (Character.isDigit(ch)) {
                sb.append(ch);
                i--;
            } else {
                if (isFirstNumber) {
                    first = new BigInteger(sb.reverse().toString());
                    sb.setLength(0);
                } else {
                    second = new BigInteger(sb.reverse().toString());
                    break;
                }
                isFirstNumber = false;
                i -= 4;
            }
        }

        return new Button<>(first, second);
    }

    private record PrizeSet(Button<BigInteger> a, Button<BigInteger> b, Button<BigInteger> target) {
    }

    private record Button<T>(T y, T x) {
    }
}
