package aoc.aoc.days;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Day04 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        var matrix = new StringMatrix(input);
        return "" + matrix.countXmasSequences();
    }

    @Override
    protected String part2Impl(String input) {
        return "" + countXmasCrosses(input.lines().toList());
    }

    @SuppressWarnings("java:S2583")
    private int countXmasCrosses(List<String> matrix) {
        int sum = 0;

        int width = matrix.getFirst().length();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                char ch = matrix.get(i).charAt(j);
                if (ch == 'A') {
                    sum += isXmasCross(matrix, i, j) ? 1 : 0;
                }
            }
        }

        return sum;
    }

    private boolean isXmasCross(List<String> matrix, int i, int j) {
        int width = matrix.getFirst().length();

        boolean isWithinBounds = i > 0 && i < width - 1 && j > 0 && j < width - 1;
        if (!isWithinBounds) {
            return false;
        }

        String str = String.valueOf(matrix.get(i - 1).charAt(j - 1)) +
                matrix.get(i - 1).charAt(j + 1) +
                matrix.get(i + 1).charAt(j - 1) +
                matrix.get(i + 1).charAt(j + 1);

        Set<String> crossTips = Set.of("SSMM", "MMSS", "SMSM", "MSMS");
        return crossTips.contains(str);
    }

    @Getter
    @Accessors(fluent = true)
    private static class StringMatrix {

        private final List<String> horizontals;
        private final List<String> verticals;
        private final List<String> diagonalsNe2Sw;
        private final List<String> diagonalsNw2Se;

        StringMatrix(String input) {
            this.horizontals = input.lines()
                    .map(line -> line.replaceAll("\\s", ""))
                    .toList();
            this.verticals = rotate90Degrees(horizontals);

            this.diagonalsNe2Sw = rotate45Degrees(horizontals);
            this.diagonalsNw2Se = rotate45Degrees(verticals);
        }

        public int countXmasSequences() {
            return count(horizontals) + count(verticals) + count(diagonalsNe2Sw) + count(diagonalsNw2Se);
        }

        private int count(List<String> matrix) {
            int sum = 0;

            String sequence = "XMAS";
            String reversedSequence = StringUtils.reverse(sequence);
            for (var line : matrix) {
                sum += StringUtils.countMatches(line, sequence)
                        + StringUtils.countMatches(line, reversedSequence);
            }

            return sum;
        }

        private List<String> rotate90Degrees(List<String> matrix) {
            List<String> result = new ArrayList<>();

            int width = matrix.getFirst().length();
            for (int i = width - 1; i >= 0; i--) {
                var sb = new StringBuilder();
                for (int j = 0; j < width; j++) {
                    sb.append(matrix.get(j).charAt(i));
                }
                result.add(sb.toString());
            }

            return result;
        }

        private List<String> rotate45Degrees(List<String> stringMatrix) {
            List<String> diagonals = new ArrayList<>();

            int width = stringMatrix.getFirst().length();
            for (int position = 0; position < width; position++) {
                var sb = new StringBuilder();

                for (int i = position / width, j = Math.min(position, width - 1); i < width && j >= 0; i++, j--) {
                    char ch = stringMatrix.get(i).charAt(j);
                    sb.append(ch);
                }

                diagonals.add(sb.toString());
            }

            for (int position = 1; position < width; position++) {
                var sb = new StringBuilder();

                for (int i = position, j = width - 1; i < width && j >= 0; i++, j--) {
                    char ch = stringMatrix.get(i).charAt(j);
                    sb.append(ch);
                }

                diagonals.add(sb.toString());
            }

            return diagonals;
        }
    }
}
