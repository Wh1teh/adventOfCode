package aoc.aoc.days.implementation;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Day03 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + getResult(input, 2);
    }

    @Override
    protected String part2Impl(String input) {
        return "" + getResult(input, 12);
    }

    private record N(int value, int position) {
    }

    private static long getResult(String input, final int maxBatteries) {
        return input.lines()
                .mapToLong(line -> {
                    var arr = line.chars().map(i -> i - '0').toArray();

                    var q = new PriorityQueue<>(Comparator.comparingInt(N::position));
                    collectRegion(arr, 0, arr.length, q, new AtomicInteger(maxBatteries));

                    long res = 0;
                    while(!q.isEmpty())
                        res = res * 10 + q.poll().value;

                    return res;
                })
                .sum();
    }

    private static void collectRegion(int[] arr, int from, int to, PriorityQueue<N> q, AtomicInteger limit) {
        if (limit.get() == 0)
            return;

        var n = findLargestInRegion(arr, from, to);
        if (n.position == -1)
            return;

        q.add(n);
        limit.decrementAndGet();

        collectRegion(arr,n.position + 1,to, q, limit);
        collectRegion(arr,from, n.position, q, limit);
    }

    private static N findLargestInRegion(int[] arr, int from, int to) {
        int largest = -1;
        int position = -1;
        for (int i = from; i < to; i++) {
            if (arr[i] > largest) {
                largest = arr[i];
                position = i;
                if (largest == 9)
                    break;
            }
        }

        return new N(largest, position);
    }
}
