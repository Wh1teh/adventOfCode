package aoc.aoc.days;

import aoc.aoc.util.Coordinate;
import aoc.aoc.util.MatrixUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static aoc.aoc.util.MatrixUtils.*;

public class Day08 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + AntinodeChecker.countAntinodes(input.lines().toList(), false);
    }

    @Override
    protected String part2Impl(String input) {
        return "" + AntinodeChecker.countAntinodes(input.lines().toList(), true);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class AntinodeChecker {

        private final List<String> matrix;
        private final Set<Coordinate> antinodes = new HashSet<>();
        private final boolean part2;

        public static int countAntinodes(List<String> matrix, boolean part2) {
            var checker = new AntinodeChecker(matrix, part2);
            for (int y = 0; y < matrix.size(); y++)
                for (int x = 0; x < matrix.getFirst().length(); x++)
                    checker.checkForAntinodes(new Coordinate(y, x));

            return checker.antinodes.size();
        }

        private void checkForAntinodes(Coordinate position) {
            char antennaType = matrix.get(position.y()).charAt(position.x());
            if (!Character.isLetterOrDigit(antennaType))
                return;

            for (int perimeterSize = 1; perimeterSize < matrix.size(); perimeterSize++)
                checkPerimeterForCompatibleNodes(position, createPerimeter(position, perimeterSize, matrix.size()));
        }

        private void checkPerimeterForCompatibleNodes(Coordinate position, MatrixUtils.Perimeter perimeter) {
            for (int y = perimeter.up(); y <= perimeter.down(); y++) {
                for (int x = perimeter.left(); x <= perimeter.right(); x++) {
                    if (isNotInPerimeter(position, perimeter, y, x))
                        continue;

                    if (isSameTypeAntenna(position, matrix.get(y).charAt(x)))
                        addAntinodes(position, y, x);
                }
            }
        }

        private void addAntinodes(Coordinate position, int y, int x) {
            int verticalStep = position.y() - y;
            int horizontalStep = position.x() - x;
            var potentialAntinode = new Coordinate(y + verticalStep * 2, x + horizontalStep * 2);

            if (part2)
                addPart2Antinodes(position, verticalStep, horizontalStep);
            else if (isWithinMatrix(potentialAntinode, matrix))
                antinodes.add(potentialAntinode);
        }

        private static boolean isNotInPerimeter(Coordinate position, MatrixUtils.Perimeter perimeter, int y, int x) {
            boolean isTopOrBottomRow = y == perimeter.up() || y == perimeter.down();
            boolean isLeftOrRightCol = x == perimeter.left() || x == perimeter.right();
            boolean isOriginalPosition = position.y() == y && position.x() == x;

            return isOriginalPosition || (!isTopOrBottomRow && !isLeftOrRightCol);
        }

        private void addPart2Antinodes(Coordinate position, int verticalStep, int horizontalStep) {
            if (isWithinMatrix(position, matrix)) {
                antinodes.add(position);
                var nextPosition = new Coordinate(position.y() + verticalStep, position.x() + horizontalStep);
                addPart2Antinodes(nextPosition, verticalStep, horizontalStep);
            }
        }

        private boolean isSameTypeAntenna(Coordinate position, char antenna) {
            return matrix.get(position.y()).charAt(position.x()) == antenna;
        }
    }
}
