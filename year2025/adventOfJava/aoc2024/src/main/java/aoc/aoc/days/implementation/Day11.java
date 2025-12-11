package aoc.aoc.days.implementation;

import aoc.aoc.cache.Memoize;
import aoc.aoc.days.interfaces.DayStringParser;
import aoc.aoc.util.GenericGraph;
import aoc.aoc.util.VisitableGraph;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiPredicate;

public class Day11 extends DayStringParser {

    private static final String PART_2_SAMPLE = """
            svr: aaa bbb
            aaa: fft
            fft: ccc
            bbb: tty
            tty: ccc
            ccc: ddd eee
            ddd: hub
            hub: fff
            eee: dac
            dac: fff
            fff: ggg hhh
            ggg: out
            hhh: out
            """;
    private static final String YOU = "you";
    private static final String OUT = "out";
    private static final String SVR = "svr";

    private static final Set<String> SPECIAL_NODES = Set.of("fft", "dac", OUT);

    @Override
    protected Object part1Impl(String input) {
        return parseGraph(input).acceptTraversalMethod(g ->
                dfsCountPaths(g, YOU, (n, __) -> OUT.equals(n), new HashSet<>())
        );
    }

    @Override
    protected Object part2Impl(String input) {
        input = !isSample() ? input : PART_2_SAMPLE;
        return parseGraph(input).acceptTraversalMethod(g ->
                dfsCountPaths(g, SVR, this::isOutAndHandleSpecialNodes, new HashSet<>())
        );
    }

    @Memoize
    protected <T> long dfsCountPaths(
            VisitableGraph<T> graph, T current, BiPredicate<T, Set<T>> end, Set<T> set
    ) {
        if (end.test(current, set))
            return 1L;

        var count = new AtomicLong(0L);
        graph.forAdjacent(current, destination -> count.addAndGet(
                dfsCountPaths(graph, destination, end, new HashSet<>(set))
        ));

        return count.get();
    }

    private boolean isOutAndHandleSpecialNodes(String node, Set<String> encountered) {
        return SPECIAL_NODES.contains(node)
               && encountered.add(node)
               && encountered.size() == 3;
    }

    private static VisitableGraph<String> parseGraph(String input) {
        var graph = new GenericGraph<String>();
        input.lines().forEach(line -> {
            var split = line.split(" ");
            var node = split[0].substring(0, split[0].length() - 1);
            Arrays.stream(split).skip(1).forEach(s -> graph.addEdge(node, s));
        });

        return graph;
    }
}
