package aoc.aoc.util;

import lombok.experimental.StandardException;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Graph<T> implements VisitableGraph<T> {

    private final Map<T, List<Edge<T>>> adjacencyList = new HashMap<>();

    @Override
    public <R> R acceptTraversalMethod(Function<VisitableGraph<T>, R> function) {
        return function.apply(this);
    }

    @Override
    public void forAdjacent(T current, Consumer<T> consumer) {
        adjacencyList.getOrDefault(current, Collections.emptyList())
                .forEach(adjacent -> consumer.accept(adjacent.destination));
    }

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
        Number distance;

        Node(T vertex, Number distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    public void addVertex(T vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(T source, T destination) {
        adjacencyList.putIfAbsent(source, new ArrayList<>());
        adjacencyList.putIfAbsent(destination, new ArrayList<>());
        adjacencyList.get(source).add(new Edge<>(destination, Integer.MIN_VALUE));
    }

    public void addEdge(T source, T destination, int weight) {
        adjacencyList.putIfAbsent(source, new ArrayList<>());
        adjacencyList.putIfAbsent(destination, new ArrayList<>());
        adjacencyList.get(source).add(new Edge<>(destination, weight));
    }

    public void addEdge(T source, T destination, double weight) {
        adjacencyList.putIfAbsent(source, new ArrayList<>());
        adjacencyList.putIfAbsent(destination, new ArrayList<>());
        adjacencyList.get(source).add(new Edge<>(destination, weight));
    }

    public void clear() {
        adjacencyList.clear();
    }

    public List<T> dijkstra(T start, Predicate<T> endCondition) {
        Map<T, Double> distances = new HashMap<>();
        Map<T, T> previousNodes = new HashMap<>();
        PriorityQueue<Node<T>> priorityQueue = new PriorityQueue<>(
                Comparator.comparingInt(node -> (int) node.distance)
        );

        for (T node : adjacencyList.keySet()) {
            distances.put(node, Double.MAX_VALUE);
            previousNodes.put(node, null);
        }
        distances.put(start, 0.0);
        priorityQueue.add(new Node<>(start, 0.0));

        while (!priorityQueue.isEmpty()) {
            Node<T> currentNode = priorityQueue.poll();
            T current = currentNode.vertex;

            if (endCondition.test(current))
                return constructPath(previousNodes, current);

            for (Edge<T> edge : adjacencyList.getOrDefault(current, new ArrayList<>())) {
                assertNonNegativeEdgeWeight(edge);

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

    public List<List<List<T>>> dijkstraEveryPath(T start, Predicate<T> endCondition) {
        Map<T, Integer> distances = new HashMap<>();
        Map<T, List<List<T>>> paths = new HashMap<>();
        PriorityQueue<Node<T>> priorityQueue = new PriorityQueue<>(
                Comparator.comparingInt(n -> (int) n.distance)
        );

        distances.put(start, 0);
        paths.put(start, new ArrayList<>());
        paths.get(start).add(new ArrayList<>(Collections.singletonList(start)));

        priorityQueue.add(new Node<>(start, 0));

        while (!priorityQueue.isEmpty()) {
            Node<T> currentNode = priorityQueue.poll();
            T current = currentNode.vertex;

            if (endCondition.test(current))
                continue;

            for (Edge<T> edge : adjacencyList.getOrDefault(current, new ArrayList<>())) {
                assertNonNegativeEdgeWeight(edge);

                int newDist = (int) currentNode.distance + edge.weight.intValue();
                if (newDist < distances.getOrDefault(edge.destination, Integer.MAX_VALUE)) {
                    distances.put(edge.destination, newDist);
                    paths.put(edge.destination, dijkstraEveryPathCreateNewPath(edge, paths, current));
                    priorityQueue.add(new Node<>(edge.destination, newDist));
                } else if (newDist == distances.getOrDefault(edge.destination, Integer.MAX_VALUE)) {
                    paths.get(current).forEach(path -> {
                        List<T> newPath = new ArrayList<>(path);
                        newPath.add(edge.destination);
                        paths.get(edge.destination).add(newPath);
                    });
                }
            }
        }

        return paths.entrySet().stream()
                .filter(e -> endCondition.test(e.getKey()))
                .map(Map.Entry::getValue)
                .toList();
    }

    private static <T> List<List<T>> dijkstraEveryPathCreateNewPath(
            Edge<T> edge, Map<T, List<List<T>>> paths, T current
    ) {
        return new ArrayList<>(paths.get(current).stream().map(path -> {
            List<T> newPath = new ArrayList<>(path);
            newPath.add(edge.destination);
            return newPath;
        }).toList());
    }

    private static <T> void assertNonNegativeEdgeWeight(Edge<T> edge) {
        if (edge.weight.intValue() < 0)
            throw new EdgeWeightException("Edge weight should not be negative for Dijkstra's algorithm");
    }

    private List<T> constructPath(Map<T, T> previousNodes, T endNode) {
        List<T> path = new LinkedList<>();
        for (T at = endNode; at != null; at = previousNodes.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    private Map<T, Integer> bfsLabel(T start, T end) {
        Map<T, Integer> depth = new HashMap<>(Map.of(start, 0));
        Deque<T> queue = new ArrayDeque<>(List.of(start));

        while (!queue.isEmpty()) {
            T current = queue.poll();
            if (current.equals(end))
                return depth;

            for (var adjacent : adjacencyList.getOrDefault(current, Collections.emptyList())) {
                var destination = adjacent.destination;
                depth.computeIfAbsent(destination, __ -> {
                    queue.add(destination);
                    return depth.get(current) + 1;
                });
            }
        }

        return depth;
    }

    private void dfsShortestPaths(T current, T end, Map<T, Integer> depth, List<T> path, List<List<T>> result) {
        path.add(current);

        if (current.equals(end)) {
            result.add(new ArrayList<>(path));
        } else {
            for (var adjacent : adjacencyList.getOrDefault(current, Collections.emptyList())) {
                var destination = adjacent.destination;
                if (depth.containsKey(destination) && depth.get(destination) == depth.get(current) + 1)
                    dfsShortestPaths(destination, end, depth, path, result);
            }
        }

        path.removeLast();
    }

    public List<List<T>> findEveryShortestPath(T start, T end) {
        Map<T, Integer> depth = bfsLabel(start, end);
        List<List<T>> result = new ArrayList<>();
        if (!depth.containsKey(end))
            return result;

        dfsShortestPaths(start, end, depth, new ArrayList<>(), result);
        return result;
    }

    public int countCycles() {
        Set<T> visited = new HashSet<>();
        Set<T> recursionStack = new HashSet<>();
        int[] cycleCount = {0};

        for (T vertex : adjacencyList.keySet()) {
            if (!visited.contains(vertex) && dfs(vertex, visited, recursionStack))
                cycleCount[0]++;
        }

        return cycleCount[0];
    }

    private boolean dfs(T current, Set<T> visited, Set<T> recursionStack) {
        visited.add(current);
        recursionStack.add(current);

        for (Edge<T> neighbor : adjacencyList.get(current)) {
            T destination = neighbor.destination;
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
            for (Edge<T> neighbor : adjacencyList.getOrDefault(current, new ArrayList<>())) {
                T destination = neighbor.destination;
                if (!visited.contains(destination)) {
                    visited.add(destination);
                    queue.add(destination);
                    reachableVertices.add(destination);
                }
            }
        }

        return reachableVertices;
    }

    @StandardException
    public static class GraphException extends RuntimeException {
    }

    @StandardException
    public static class EdgeWeightException extends GraphException {
    }
}
