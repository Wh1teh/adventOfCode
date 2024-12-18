package aoc.aoc.util;

import lombok.experimental.StandardException;

import java.util.*;
import java.util.function.Predicate;

public class Graph<T> {

    private final Map<T, List<Edge<T>>> adjList = new HashMap<>();

    public static class Edge<T> {
        T destination;
        Number weight;

        public Edge(T destination, Number weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    private static class Node<T> {
        T vertex;
        double distance;

        Node(T vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    public void addVertex(T vertex) {
        adjList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(T source, T destination) {
        adjList.putIfAbsent(source, new ArrayList<>());
        adjList.putIfAbsent(destination, new ArrayList<>());
        adjList.get(source).add(new Edge<>(destination, Integer.MIN_VALUE));
    }

    public void addEdge(T source, T destination, int weight) {
        adjList.putIfAbsent(source, new ArrayList<>());
        adjList.putIfAbsent(destination, new ArrayList<>());
        adjList.get(source).add(new Edge<>(destination, weight));
    }

    public void clear() {
        adjList.clear();
    }

    public List<T> dijkstra(T start, Predicate<T> endCondition) {
        Map<T, Double> distances = new HashMap<>();
        Map<T, T> previousNodes = new HashMap<>();
        PriorityQueue<Node<T>> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.distance));

        for (T node : adjList.keySet()) {
            distances.put(node, Double.POSITIVE_INFINITY);
            previousNodes.put(node, null);
        }
        distances.put(start, 0.0);
        priorityQueue.add(new Node<>(start, 0.0));

        while (!priorityQueue.isEmpty()) {
            Node<T> currentNode = priorityQueue.poll();
            T current = currentNode.vertex;

            if (endCondition.test(current)) {
                return constructPath(previousNodes, current);
            }

            for (Edge<T> edge : adjList.getOrDefault(current, new ArrayList<>())) {
                if (edge.weight.doubleValue() < 0)
                    throw new EdgeWeightException("Edge weight should not be negative for Dijkstra's algorithm");

                double newDist = distances.get(current) + edge.weight.doubleValue();
                if (newDist < distances.get(edge.destination)) {
                    distances.put(edge.destination, newDist);
                    previousNodes.put(edge.destination, current);
                    priorityQueue.add(new Node<>(edge.destination, newDist));
                }
            }
        }

        return Collections.emptyList();
    }

    private List<T> constructPath(Map<T, T> previousNodes, T endNode) {
        List<T> path = new LinkedList<>();
        for (T at = endNode; at != null; at = previousNodes.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
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

        for (Edge<T> neighbor : adjList.get(current)) {
            var destination = neighbor.destination;
            if (!visited.contains(destination)) {
                if (dfs(destination, visited, recursionStack)) {
                    return true;
                }
            } else if (recursionStack.contains(destination)) {
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
            for (Edge<T> neighbor : adjList.getOrDefault(current, new ArrayList<>())) {
                var destination = neighbor.destination;
                if (!visited.contains(destination)) {
                    visited.add(destination);
                    queue.add(destination);
                    reachableVertices.add(destination);
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

    @StandardException
    public static class GraphException extends RuntimeException {
    }

    @StandardException
    public static class EdgeWeightException extends GraphException {
    }
}
