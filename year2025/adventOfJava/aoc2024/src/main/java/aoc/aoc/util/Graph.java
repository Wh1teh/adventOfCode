package aoc.aoc.util;

public interface Graph<T> {

    void addVertex(T vertex);

    void addEdge(T source, T destination);

    void addEdge(T source, T destination, int weight);

    void addEdge(T source, T destination, double weight);

    void clear();
}
