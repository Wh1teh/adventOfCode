package aoc.aoc.days.implementation;

import aoc.aoc.days.enums.Part;
import aoc.aoc.util.Utils;

import java.util.*;

import static aoc.aoc.days.enums.Part.*;

@SuppressWarnings("java:S6916") // false positive
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
                    boolean isOutOfRange = stack != 0;
                    if (isOutOfRange)
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

    private record Parsed(List<long[]> ranges, List<Long> ids) {
    }

    private static PriorityQueue<Id> buildPriorityQueue(String input, Part part) {
        var parsed = parseRangesAndIds(input, part);
        var heap = new PriorityQueue<Id>();

        addRanges(parsed, heap);
        if (part == PART_1)
            addIds(parsed, heap);

        return heap;
    }

    private static Parsed parseRangesAndIds(String input, Part part) {
        var split = input.split("\\R{2}");
        return new Parsed(parseRanges(split), part == PART_1 ? parseIds(split) : null);
    }

    private static List<Long> parseIds(String[] split) {
        return split[1].lines().map(Long::parseLong).toList();
    }

    private static List<long[]> parseRanges(String[] split) {
        return split[0].lines()
                .map(line -> {
                    var s = line.split("-");
                    return Utils.arrayOf(Long.parseLong(s[0]), Long.parseLong(s[1]));
                })
                .toList();
    }

    private static void addRanges(Parsed parsed, PriorityQueue<Id> heap) {
        for (var range : parsed.ranges) {
            var start = new Id(range[0], Boundary.START);
            var end = new Id(range[1], Boundary.END);
            heap.offer(start);
            heap.offer(end);
        }
    }

    private static void addIds(Parsed parsed, PriorityQueue<Id> heap) {
        for (Long id : parsed.ids)
            heap.offer(new Id(id, Boundary.NOT));
    }
}
