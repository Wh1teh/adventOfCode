package aoc.aoc.days.implementation;

import aoc.aoc.days.interfaces.DayStringParser;
import aoc.aoc.util.Graph;
import aoc.aoc.util.Pair;

import java.util.*;

public class Day08 extends DayStringParser {

    private static final int TOP_CONNECTIONS = 3;
    private static final int SAMPLE_LIMIT = 10;
    private static final int REAL_LIMIT = 1000;

    @Override
    protected Object part1Impl(String input) {
        return solve2(input);
    }

    @Override
    protected Object part2Impl(String input) {
        return solve2(input);
    }

    private record Box(int x, int y, int z) {
    }

    private long solve2(String input) {
        var boxes = parseBoxes(input);
        var graph = new Graph<Box>();

        var sorted = getSortedPairs(boxes);

        int connectionLimit = isSample() ? SAMPLE_LIMIT : REAL_LIMIT;
        for (var pair : sorted.values()) {
            var from = pair.first();
            var to = pair.second();

            graph.addEdge(from, to);
            graph.addEdge(to, from);

            if (isPart2()) {
                if (graph.allReachableFrom(from).size() == boxes.size())
                    return (long) from.x() * to.x();
            } else if (--connectionLimit <= 0) {
                break;
            }
        }

        return getTopConnections(boxes, graph);
    }

    private static TreeMap<Double, Pair<Box, Box>> getSortedPairs(List<Box> boxes) {
        var sorted = new TreeMap<Double, Pair<Box, Box>>();
        for (int i = 0; i < boxes.size(); i++) {
            for (int j = i + 1; j < boxes.size(); j++) {
                Box from = boxes.get(i);
                Box to = boxes.get(j);

                if (from.equals(to))
                    continue;

                double dist = distance(from, to);
                var pair = new Pair<>(from, to);
                sorted.put(dist, pair);
            }
        }
        return sorted;
    }

    private static double distance(Box a, Box b) {
        long dx = (long) a.x() - b.x();
        long dy = (long) a.y() - b.y();
        long dz = (long) a.z() - b.z();

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private static long getTopConnections(List<Box> boxes, Graph<Box> graph) {
        var all = new HashSet<Set<Box>>();
        for (Box box : boxes)
            all.add(graph.allReachableFrom(box));

        return all.stream()
                .map(Set::size)
                .sorted(Comparator.reverseOrder())
                .limit(TOP_CONNECTIONS)
                .mapToLong(Integer::longValue)
                .reduce(1L, (a, b) -> a * b);
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
