package aoc.aoc.days.implementation;

import aoc.aoc.days.enums.Part;

import java.util.*;

import static aoc.aoc.days.enums.Part.*;

public class Day05 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        var heap = buildPriorityQueue(input, PART_1);

        int freshIds = 0;
        int stack = 0;
        while (!heap.isEmpty()) {
            var id = heap.poll();
            switch (id.boundary) {
                case START -> stack++;
                case END -> stack--;
                case NOT -> {
                    boolean isNotOutOfRange = stack != 0;
                    if (isNotOutOfRange)
                        ++freshIds;
                }
            }
        }

        return "" + freshIds;
    }

    @Override
    protected String part2Impl(String input) {
        var heap = buildPriorityQueue(input, PART_2);

        long freshIds = 0;
        var stack = new ArrayDeque<Long>();
        while (!heap.isEmpty()) {
            var id = heap.poll();
            switch (id.boundary) {
                case START -> stack.push(id.number);
                case END -> {
                    var pop = stack.pop();
                    if (stack.isEmpty())
                        freshIds += id.number + 1 - pop;
                }
                case NOT -> {/*ignore*/}
            }
        }

        return "" + freshIds;
    }

    private enum Boundary {
        START,
        NOT,
        END,
    }

    private record Id(long number, Boundary boundary) implements Comparable<Id> {
        @Override
        public int compareTo(Id o) {
            int c = Long.compare(number, o.number);
            return c != 0 ? c : boundary.compareTo(o.boundary);
        }
    }

    private static PriorityQueue<Id> buildPriorityQueue(String input, Part part) {
        var heap = new PriorityQueue<Id>();
        var split = input.split("\\R{2}");

        parseAndAddRanges(heap, split);
        if (part == PART_1)
            parseAndAddIds(heap, split);

        return heap;
    }

    private static void parseAndAddIds(PriorityQueue<Id> heap, String[] split) {
        split[1].lines()
                .forEach(line -> heap.offer(new Id(Long.parseLong(line), Boundary.NOT)));
    }

    private static void parseAndAddRanges(PriorityQueue<Id> heap, String[] split) {
        split[0].lines()
                .forEach(line -> {
                    var s = line.split("-");
                    heap.offer(new Id(Long.parseLong(s[0]), Boundary.START));
                    heap.offer(new Id(Long.parseLong(s[1]), Boundary.END));
                });
    }
}
