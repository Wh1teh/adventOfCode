package com.aoc.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Graph<T> {
    private Map<T, List<T>> adjacencyList;

        public Graph() {
            this.adjacencyList = new HashMap<>();
        }

        public void addEdge(T source, T destination) {
            adjacencyList.computeIfAbsent(source, k -> new ArrayList<>()).add(destination);
            adjacencyList.computeIfAbsent(destination, k -> new ArrayList<>()).add(source);
        }

        public List<T> getNeighbors(T node) {
            return adjacencyList.getOrDefault(node, Collections.emptyList());
        }

        public Set<T> bfs(T startNode) {
            Set<T> visited = new HashSet<>();
            Queue<T> queue = new LinkedList<>();

            visited.add(startNode);
            queue.add(startNode);

            while (!queue.isEmpty()) {
                T current = queue.poll();
                for (T neighbor : getNeighbors(current)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }

            return visited;
        }
}
