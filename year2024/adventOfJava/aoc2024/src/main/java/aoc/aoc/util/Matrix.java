package aoc.aoc.util;

public interface Matrix<T> {

    T get(int y, int x);

    T get(Coordinate position);

    void iterate(MatrixConsumer<T> consumer);

    int size();

    @FunctionalInterface
    interface MatrixConsumer<T> {
        void accept(T t, int y, int x);
    }
}
