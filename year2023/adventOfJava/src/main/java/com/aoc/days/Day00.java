package com.aoc.days;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Day00 extends Day {

    @Override
    public String solveFirstPart() {
        List<Integer> elves = new ArrayList<>();
        elves.add(0);

        // calculate total calories
        int index = 0;
        int largest = 0;
        for (String line : LINES) {
            if (line.isEmpty()) {
                largest = elves.get(index) > largest ? elves.get(index) : largest;
                ++index;
                elves.add(0);
            } else {
                elves.set(index, elves.get(index) + Integer.parseInt(line));
            }
        }

        return "" + largest;
    }

    @Override
    public String solveSecondPart() {
        Queue<Integer> elves = new PriorityQueue<>(Collections.reverseOrder());

        // calculate total calories
        int elf = 0;
        for (String line : LINES) {
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

        return "" + largest;
    }

}