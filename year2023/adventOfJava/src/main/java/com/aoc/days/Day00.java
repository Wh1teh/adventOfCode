package com.aoc.days;

import com.aoc.util.ReadFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Day00 implements DayInterface {

    public String solve(int part) {
        List<String> lines = ReadFile.getData(
                "src/main/java/com/aoc/data/day00" + (part == 0 ? "_sample" : "") + ".txt");
        if (lines == null || lines.size() == 0) {
            return "Day 00 is not available";
        }
        StringBuilder str = new StringBuilder();

        str = switch (part) {
            case 0 -> solveSample(lines);
            case 1 -> solveFirstPart(lines);
            case 2 -> solveSecondPart(lines);
            default -> throw new IllegalArgumentException("Unexpected value: " + part);
        };

        if (str.isEmpty())
            return "Day 00 part " + part + " is Unimplemented";
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

        List<Integer> elves = new ArrayList<>();
        elves.add(0);

        // calculate total calories
        int index = 0;
        int largest = 0;
        for (String line : lines) {
            if (line.isEmpty()) {
                largest = elves.get(index) > largest ? elves.get(index) : largest;
                ++index;
                elves.add(0);
            } else {
                elves.set(index, elves.get(index) + Integer.parseInt(line));
            }
        }
        result.append(largest);

        return result;
    }

    public StringBuilder solveSecondPart(List<String> lines) {
        StringBuilder result = new StringBuilder();

        Queue<Integer> elves = new PriorityQueue<>(Collections.reverseOrder());

        // calculate total calories
        int elf = 0;
        for (String line : lines) {
            if(line.isEmpty()) {
                elves.add(elf);
                elf = 0;
            } else {
                elf += Integer.parseInt(line);
            }
        }
        elves.add(elf); // add last elf

        // get top 3 from prioQ
        int largest = 0;
        for (int i = 0; i < 3; i++) {
            largest += elves.poll();
        }
        result.append(largest);

        return result;
    }

}