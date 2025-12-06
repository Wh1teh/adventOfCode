package aoc.aoc.days.implementation;

import aoc.aoc.util.GenericMatrix;
import aoc.aoc.util.Operand;
import aoc.aoc.util.StringMatrix;
import aoc.aoc.util.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day06 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        var temp = input.lines()
                .map(line -> line.trim().split("\\s+"))
                .toList();
        List<List<Integer>> numbers = new ArrayList<>();
        for (int i = 0; i < temp.size() - 1; i++) {
            var line = temp.get(i);
            numbers.add(Arrays.stream(line).map(Integer::parseInt).toList());
        }
        var operands = Arrays.stream(temp.getLast())
                .map(Operand::fromString)
                .toList();

        long sum = 0L;
        for (int x = 0; x < numbers.getFirst().size(); x++) {
            var operand = operands.get(x);
            long current = numbers.getFirst().get(x);
            for (int y = 1; y < numbers.size(); y++) {
                int next = numbers.get(y).get(x);
                switch (operand) {
                    case ADD -> current += next;
                    case SUB -> current -= next;
                    case MUL -> current *= next;
                    case DIV -> current /= next;
                }
            }
            sum += current;
        }

        return "" + sum;
    }

    @Override
    protected String part2Impl(String input) {
        var lines = input.lines().toList();
        int widestRow = 0;
        for (String line : lines) {
            if (line.length() > widestRow)
                widestRow = line.length();
        }

        var numbers = new ArrayList<Integer>();
        long sum = 0L;
        for (int col = widestRow - 1; col >= -1; col--) {
            var sb = new StringBuilder();
            for (int row = 0; row < lines.size() - 1; row++) {
                try {
                    sb.append(lines.get(row).charAt(col));
                } catch (IndexOutOfBoundsException e) {
                    // dont care
                }
            }
            if (StringUtils.isBlank(sb)) {
                var operand = Operand.fromChar(lines.getLast().charAt(col + 1));
                long current = numbers.getFirst();
                for (int i = 1; i < numbers.size(); i++) {
                    int next = numbers.get(i);
                    switch (operand) {
                        case ADD -> current += next;
                        case SUB -> current -= next;
                        case MUL -> current *= next;
                        case DIV -> current /= next;
                    }
                }
                sum += current;
                numbers.clear();
            } else {
                numbers.add(Integer.parseInt(sb.toString().trim()));
            }
        }

        return "" + sum;
    }
}
