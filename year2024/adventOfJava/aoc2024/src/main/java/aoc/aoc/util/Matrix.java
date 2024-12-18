package aoc.aoc.util;

import java.util.function.BiConsumer;

public interface Matrix<T> {

    T get(int y, int x);

    T get(Coordinate position);

    T set(Coordinate position, T element);

    T set(int y, int x, T element);

    void iterate(MatrixConsumer<T> consumer);

    void iterate(BiConsumer<T, Coordinate> consumer);

    int size();

    @FunctionalInterface
    interface MatrixConsumer<T> {
        void accept(T t, int y, int x);
    }
}
