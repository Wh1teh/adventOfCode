package com.aoc.days;

import com.aoc.util.ReadFile;
import com.aoc.util.Coordinates;
import com.aoc.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day03 implements DayInterface {

    public String solve(int part) {
        List<String> lines = ReadFile.getData(
                "src/main/java/com/aoc/data/day03" + (part == 0 ? "_sample" : "") + ".txt");
        if (lines == null || lines.size() == 0) {
            return "Day 03 is not available";
        }
        StringBuilder str = new StringBuilder();

        str = switch (part) {
            case 0 -> solveSample(lines);
            case 1 -> solveFirstPart(lines);
            case 2 -> solveSecondPart(lines);
            default -> throw new IllegalArgumentException("Unexpected value: " + part);
        };

        if (str.isEmpty())
            return "Day 03 part " + part + " is Unimplemented";
        return str.toString();
    }

    public StringBuilder solveSample(List<String> lines) {
        StringBuilder result = new StringBuilder();

        result.append(solveFirstPart(lines));
        result.append("\n---\n");
        result.append(solveSecondPart(lines));

        return result;
    }

    /*
     * --- Day 3: Gear Ratios ---
     * You and the Elf eventually reach a gondola lift station; he says the gondola
     * lift will take you up to the water source, but this is as far as he can bring
     * you. You go inside.
     * 
     * It doesn't take long to find the gondolas, but there seems to be a problem:
     * they're not moving.
     * 
     * "Aaah!"
     * 
     * You turn around to see a slightly-greasy Elf with a wrench and a look of
     * surprise.
     * "Sorry, I wasn't expecting anyone! The gondola lift isn't working right now; it'll still be a while before I can fix it."
     * You offer to help.
     * 
     * The engineer explains that an engine part seems to be missing from the
     * engine, but nobody can figure out which one. If you can add up all the part
     * numbers in the engine schematic, it should be easy to work out which part is
     * missing.
     * 
     * The engine schematic (your puzzle input) consists of a visual representation
     * of the engine. There are lots of numbers and symbols you don't really
     * understand, but apparently any number adjacent to a symbol, even diagonally,
     * is a "part number" and should be included in your sum. (Periods (.) do not
     * count as a symbol.)
     * 
     * Here is an example engine schematic:
     * 
     * 467..114..
     * ...*......
     * ..35..633.
     * ......#...
     * 617*......
     * .....+.58.
     * ..592.....
     * ......755.
     * ...$.*....
     * .664.598..
     * In this schematic, two numbers are not part numbers because they are not
     * adjacent to a symbol: 114 (top right) and 58 (middle right). Every other
     * number is adjacent to a symbol and so is a part number; their sum is 4361.
     * 
     * Of course, the actual engine schematic is much larger. What is the sum of all
     * of the part numbers in the engine schematic?
     * 
     * 
     */
    public StringBuilder solveFirstPart(List<String> lines) {
        StringBuilder result = new StringBuilder();

        List<Integer> numbers = new ArrayList<>();
        int index = 0;
        for (String line : lines) {
            numbers.addAll(getValidNumbers(lines, line, index++));
        }

        int sum = 0;
        for (Integer num : numbers) {
            sum += num;
        }

        return result.append(sum);
    }

    private List<Integer> getValidNumbers(List<String> lines, String line, int lineIndex) {
        List<Integer> numbers = new ArrayList<>();

        for (int index = 0; index < line.length(); index++) {
            char ch = line.charAt(index);

            if (Character.isDigit(ch)) {
                // get number and check adjacent
                String num = parseNumber(line, index);
                int numEnd = index + num.length() - 1;
                if (isAdjacentToSymbol(lines, lineIndex, index, numEnd)) {
                    numbers.add(Integer.parseInt(num));
                }
                index += num.length() - 1; // hop over number
            }
        }

        return numbers;
    }

    private boolean isAdjacentToSymbol(List<String> lines, int lineIndex, int start, int end) {
        String line = lines.get(lineIndex);

        // ensure non-negative indexes
        start = start == 0 ? 1 : start;
        int verticalStart = lineIndex == 0 ? 0 : lineIndex - 1;
        int verticalEnd = lineIndex == lines.size() - 1 ? lineIndex : lineIndex + 1;

        while (start <= end && start < line.length()) {
            for (int i = verticalStart; i <= verticalEnd; i++) {
                if (findSymbol(lines.get(i), start - 1, end + 1)) {
                    return true;
                }
            }
            start++;
        }

        return false;
    }

    private boolean findSymbol(String line, int start, int end) {
        boolean result = false;

        while (start <= end && start < line.length()) {
            char ch = line.charAt(start);
            if (ch != '.' && !Character.isDigit(ch)) {
                result = true;
                break;
            }
            start++;
        }

        return result;
    }

    private String parseNumber(String line, int index) {
        StringBuilder number = new StringBuilder();
        while (index < line.length() && Character.isDigit(line.charAt(index))) {
            number.append(line.charAt(index++));
        }
        return number.toString();
    }

    /*
     * --- Part Two ---
     * The engineer finds the missing part and installs it in the engine! As the
     * engine springs to life, you jump in the closest gondola, finally ready to
     * ascend to the water source.
     * 
     * You don't seem to be going very fast, though. Maybe something is still wrong?
     * Fortunately, the gondola has a phone labeled "help", so you pick it up and
     * the engineer answers.
     * 
     * Before you can explain the situation, she suggests that you look out the
     * window. There stands the engineer, holding a phone in one hand and waving
     * with the other. You're going so slowly that you haven't even left the
     * station. You exit the gondola.
     * 
     * The missing part wasn't the only issue - one of the gears in the engine is
     * wrong. A gear is any * symbol that is adjacent to exactly two part numbers.
     * Its gear ratio is the result of multiplying those two numbers together.
     * 
     * This time, you need to find the gear ratio of every gear and add them all up
     * so that the engineer can figure out which gear needs to be replaced.
     * 
     * Consider the same engine schematic again:
     * 
     * 467..114..
     * ...*......
     * ..35..633.
     * ......#...
     * 617*......
     * .....+.58.
     * ..592.....
     * ......755.
     * ...$.*....
     * .664.598..
     * In this schematic, there are two gears. The first is in the top left; it has
     * part numbers 467 and 35, so its gear ratio is 16345. The second gear is in
     * the lower right; its gear ratio is 451490. (The * adjacent to 617 is not a
     * gear because it is only adjacent to one part number.) Adding up all of the
     * gear ratios produces 467835.
     * 
     * What is the sum of all of the gear ratios in your engine schematic?
     */
    public StringBuilder solveSecondPart(List<String> lines) {
        StringBuilder result = new StringBuilder();

        Map<Coordinates, Integer> numbers = getCoordinatedNumbers(lines);
        Map<Coordinates, Character> gears = getCoordinatedGears(lines);
        List<List<Integer>> pairs = getGearedPairs(numbers, gears);

        int sum = 0;
        for (List<Integer> pair : pairs) {
            sum += pair.get(0) * pair.get(1);
        }

        return result.append(sum);
    }

    private List<List<Integer>> getGearedPairs(Map<Coordinates, Integer> numbers, Map<Coordinates, Character> gears) {
        List<List<Integer>> pairs = new ArrayList<>();

        for (Coordinates gearC : gears.keySet()) {
            List<Integer> gearNumbers = new ArrayList<>();

            for (Coordinates numC : numbers.keySet()) {
                if (gearC.partiallyOverlaps(numC)) {
                    gearNumbers.add(numbers.get(numC));
                }
            }

            if (gearNumbers.size() == 2) {
                // only add two gears that have two numbers
                pairs.add(gearNumbers);
            }
        }

        return pairs;
    }

    private Map<Coordinates, Character> getCoordinatedGears(List<String> lines) {
        Map<Coordinates, Character> symbols = new HashMap<>();

        int lineIndex = 0;
        for (String line : lines) {
            for (int i = 0; i < line.length(); i++) {
                Character ch = line.charAt(i);

                if (ch == '*') {
                    // add padding to coordinates
                    Coordinates coords = new Coordinates(lineIndex - 1, lineIndex + 1, i - 1, i + 1);
                    symbols.put(coords, ch);
                }
            }
            lineIndex++;
        }

        return symbols;
    }

    private Map<Coordinates, Integer> getCoordinatedNumbers(List<String> lines) {
        Map<Coordinates, Integer> numbers = new HashMap<>();

        int lineIndex = 0;
        for (String line : lines) {
            for (int index = 0; index < line.length(); index++) {
                char ch = line.charAt(index);

                if (Character.isDigit(ch)) {
                    // get number and check adjacent
                    String num = parseNumber(line, index);
                    int numEnd = index + num.length() - 1;

                    Coordinates coords = new Coordinates(lineIndex, lineIndex, index, numEnd);
                    numbers.put(coords, Integer.parseInt(num));

                    index += num.length() - 1; // hop over number
                }
            }
            lineIndex++;
        }

        return numbers;
    }

}