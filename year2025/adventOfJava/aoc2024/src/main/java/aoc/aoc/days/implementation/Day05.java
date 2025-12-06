package aoc.aoc.days.implementation;

import aoc.aoc.days.enums.Part;
import aoc.aoc.days.interfaces.DayStringParser;

import java.util.*;

import static aoc.aoc.days.enums.Part.*;

public class Day05 extends DayStringParser {

    @Override
    protected String part1Impl(String input) {
        return "" + countIds(input, PART_1);
    }

    @Override
    protected String part2Impl(String input) {
        return "" + countIds(input, PART_2);
    }

    private static long countIds(String input, Part part) {
        long allFreshIds = 0;
        int freshIngredients = 0;
        var activeRanges = new ArrayDeque<Long>();

        for (var id : buildSortedList(input, part)) {
            switch (id.boundary) {
                case START -> activeRanges.push(id.number);
                case END -> {
                    var pop = activeRanges.pop();
                    if (activeRanges.isEmpty())
                        allFreshIds += id.number + 1 - pop;
                }
                case NOT -> {
                    boolean isNotOutOfRange = !activeRanges.isEmpty();
                    if (isNotOutOfRange)
                        ++freshIngredients;
                }
            }
        }

        return part == PART_1 ? freshIngredients : allFreshIds;
    }

    private enum Boundary {
        START,
        NOT,
        END,
    }

    private record Id(long number, Boundary boundary) {
    }

    private static List<Id> buildSortedList(String input, Part part) {
        var split = input.split("\\R{2}");
        var ids = new ArrayList<Id>();

        parseAndAddRanges(ids, split);
        if (part == PART_1)
            parseAndAddIds(ids, split);

        ids.sort(Comparator.comparingLong(Id::number)
                .thenComparing(id -> id.boundary));
        return ids;
    }

    private static void parseAndAddIds(List<Id> ids, String[] split) {
        split[1].lines()
                .forEach(line -> ids.add(new Id(Long.parseLong(line), Boundary.NOT)));
    }

    private static void parseAndAddRanges(List<Id> ids, String[] split) {
        split[0].lines()
                .forEach(line -> {
                    var s = line.split("-");
                    ids.add(new Id(Long.parseLong(s[0]), Boundary.START));
                    ids.add(new Id(Long.parseLong(s[1]), Boundary.END));
                });
    }
}
