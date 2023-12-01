package com.aoc.days;

import com.aoc.util.ReadFile;

import java.util.List;

public class Day14 implements DayInterface {

    public String solve(int part) {
        List<String> lines = ReadFile.getData(
                "src/main/java/com/aoc/data/day14" + (part == 0 ? "_sample" : "") + ".txt");
        if (lines == null || lines.size() == 0) {
            return "Day 14 is not available";
        }
        StringBuilder str = new StringBuilder();

        str = switch (part) {
            case 0 -> solveSample(lines);
            case 1 -> solveFirstPart(lines);
            case 2 -> solveSecondPart(lines);
            default -> throw new IllegalArgumentException("Unexpected value: " + part);
        };

        if (str.isEmpty())
            return "Day 14 part " + part + " is Unimplemented";
        return str.toString();
    }

    public StringBuilder solveSample(List<String> lines) {
        StringBuilder result = new StringBuilder();

        result.append(solveFirstPart(lines));
        result.append("\n---\n");
        result.append(solveSecondPart(lines));

        return result;
    }

    public StringBuilder solveFirstPart(List<String> lines) {
        StringBuilder result = new StringBuilder();

        return result;
    }

    public StringBuilder solveSecondPart(List<String> lines) {
        StringBuilder result = new StringBuilder();

        return result;
    }

}