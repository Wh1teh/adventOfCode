package aoc.aoc.days.implementation;

import aoc.aoc.days.interfaces.DayStringParser;
import aoc.aoc.util.Coordinates3D;
import aoc.aoc.util.DisjointSet;

import java.util.*;

import static aoc.aoc.util.Utils.Math.euclideanDistance;

public class Day08 extends DayStringParser {

    private static final int TOP_CONNECTIONS = 3;
    private static final int SAMPLE_LIMIT = 10;
    private static final int REAL_LIMIT = 1000;

    @Override
    protected Object part1Impl(String input) {
        return findOptimalConnections(input);
    }

    @Override
    protected Object part2Impl(String input) {
        return findOptimalConnections(input);
    }

    private record Box(int x, int y, int z) implements Coordinates3D {
    }

    record BoxPair(Box from, Box to, double distance) implements Comparable<BoxPair> {
        @Override
        public int compareTo(BoxPair o) {
            return Double.compare(this.distance, o.distance);
        }
    }

    private long findOptimalConnections(String input) {
        var boxes = parseBoxes(input);
        var heap = getPairHeap(boxes);
        var disjointSet = new DisjointSet<Box>();
        for (var b : boxes)
            disjointSet.make(b);

        int connectionLimit = isSample() ? SAMPLE_LIMIT : REAL_LIMIT;
        while (!heap.isEmpty()) {
            var pair = heap.poll();
            var from = pair.from;
            var to = pair.to;

            disjointSet.union(from, to);

            if (isPart2() && disjointSet.components() == 1)
                return (long) from.x * to.x;
            else if (isPart1() && --connectionLimit <= 0)
                break;
        }

        return getTopConnections(boxes, disjointSet);
    }

    private static long getTopConnections(List<Box> boxes, DisjointSet<Box> disjointSet) {
        var sizes = new HashMap<Box, Integer>();
        for (var box : boxes) {
            var root = disjointSet.find(box);
            sizes.merge(root, 1, Integer::sum);
        }

        return sizes.values().stream()
                .sorted(Comparator.reverseOrder())
                .limit(TOP_CONNECTIONS)
                .mapToLong(Integer::longValue)
                .reduce(1L, (a, b) -> a * b);
    }

    private static PriorityQueue<BoxPair> getPairHeap(List<Box> boxes) {
        var list = new ArrayList<BoxPair>();

        for (int i = 0; i < boxes.size(); i++) {
            var from = boxes.get(i);
            for (int j = i + 1; j < boxes.size(); j++) {
                if (i == j)
                    continue;

                var to = boxes.get(j);
                list.add(new BoxPair(from, to, euclideanDistance(from, to)));
            }
        }

        return new PriorityQueue<>(list);
    }

    private static List<Box> parseBoxes(String input) {
        return input.lines()
                .map(line -> {
                    var split = Arrays.stream(line.split(","))
                            .mapToInt(Integer::parseInt).toArray();
                    return new Box(split[0], split[1], split[2]);
                })
                .toList();
    }
}
