package aoc.aoc.util;

import java.util.*;

public class Graph<T> {

    private final Map<T, List<T>> adjList = new HashMap<>();

    public void addVertex(T vertex) {
        adjList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(T source, T destination) {
        adjList.putIfAbsent(source, new ArrayList<>());
        adjList.putIfAbsent(destination, new ArrayList<>());
        adjList.get(source).add(destination);
    }

    public int countCycles() {
        Set<T> visited = new HashSet<>();
        Set<T> recursionStack = new HashSet<>();
        int[] cycleCount = {0};

        for (T vertex : adjList.keySet()) {
            if (!visited.contains(vertex) && dfs(vertex, visited, recursionStack))
                cycleCount[0]++;
        }

        return cycleCount[0];
    }

    private boolean dfs(T current, Set<T> visited, Set<T> recursionStack) {
        visited.add(current);
        recursionStack.add(current);

        for (T neighbor : adjList.get(current)) {
            if (!visited.contains(neighbor)) {
                if (dfs(neighbor, visited, recursionStack)) {
                    return true;
                }
            } else if (recursionStack.contains(neighbor)) {
                return true;
            }
        }

        recursionStack.remove(current);
        return false;
    }

    public Set<T> allReachableFrom(T start) {
        return bfs(start);
    }

    private Set<T> bfs(T start) {
        Set<T> visited = new HashSet<>();
        Queue<T> queue = new LinkedList<>();
        Set<T> reachableVertices = new HashSet<>();

        queue.add(start);
        visited.add(start);
        reachableVertices.add(start);

        while (!queue.isEmpty()) {
            T current = queue.poll();
            for (T neighbor : adjList.getOrDefault(current, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    reachableVertices.add(neighbor);
                }
            }
        }

        return reachableVertices;
    }

    public void printGraph() {
        for (var vertex : adjList.entrySet()) {
            System.out.println(vertex + " -> " + adjList.get(vertex.getKey()));
        }
    }
}
