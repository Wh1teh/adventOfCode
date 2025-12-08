package aoc.aoc.util;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Accessors(fluent = true)
public class DisjointSet<T> {

    private final Map<T, T> forest = new HashMap<>();
    private final Map<T, Integer> rank = new HashMap<>();
    @Getter
    private int components = 0;

    public void make(T x) {
        if (isAlreadyInTheForest(x))
            return;

        forest.put(x, x);
        rank.put(x, 0);
        components++;
    }

    public T find(T x) {
        T parent = forest.get(x);
        if (!areInSameSet(parent, x))
            forest.put(x, find(parent));

        return forest.get(x);
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean union(T a, T b) {
        T x = find(a);
        T y = find(b);
        if (areInSameSet(x, y))
            return false;

        int xRank = rank.get(x);
        int yRank = rank.get(y);
        if (xRank < yRank) {
            forest.put(x, y);
        } else if (xRank > yRank) {
            forest.put(y, x);
        } else {
            forest.put(y, x);
            rank.put(x, xRank + 1);
        }

        components--;
        return true;
    }

    private boolean isAlreadyInTheForest(T x) {
        return forest.containsKey(x);
    }

    private static <T> boolean areInSameSet(T a, T b) {
        return a.equals(b);
    }
}
