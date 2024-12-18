package aoc.aoc.util;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class GenericMatrix<T> implements Matrix<T> {

    private final List<List<T>> matrix;
    private final int height;
    private final int width;

    public GenericMatrix(Supplier<T> initialValues, int widthAndHeight) {
        this(initialValues, widthAndHeight, widthAndHeight);
    }

    public GenericMatrix(Supplier<T> initialValues, int height, int width) {
        this.height = height;
        this.width = width;
        this.matrix = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            var horizontal = new ArrayList<T>();
            for (int j = 0; j < width; j++) {
                horizontal.add(initialValues.get());
            }
            matrix.add(horizontal);
        }
    }

    public static Matrix<Character> charMatrix(String input) {
        var matrix = new ArrayList<List<Character>>(
                input.lines()
                        .map(line -> new ArrayList<>(
                                line.chars()
                                        .mapToObj(c -> (char) c)
                                        .toList()))
                        .toList()
        );
        return new GenericMatrix<>(matrix, matrix.size(), matrix.getFirst().size());
    }

    @Override
    public T get(int y, int x) {
        return matrix.get(y).get(x);
    }

    @Override
    public T get(Coordinate position) {
        return matrix.get(position.y()).get(position.x());
    }

    @Override
    public T set(Coordinate position, T element) {
        return set(position.y(), position.x(), element);
    }

    @Override
    public T set(int y, int x, T element) {
        return matrix.get(y).set(x, element);
    }

    private T setPosition(Coordinate position, T element) {
        return setPosition(position.y(), position.x(), element);
    }

    private T setPosition(int y, int x, T element) {
        return matrix.get(y).set(x, element);
    }

    public void swap(Coordinate first, Coordinate second) {
        T swappedElement = setPosition(first, get(second));
        setPosition(second, swappedElement);
    }

    @Override
    public void iterate(MatrixConsumer<T> consumer) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                consumer.accept(get(y, x), y, x);
            }
        }
    }

    @Override
    public void iterate(BiConsumer<T, Coordinate> consumer) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                consumer.accept(get(y, x), new Coordinate(y, x));
            }
        }
    }

    @Override
    public int size() {
        return width; // TODO need to rethink this on interface level
    }
}
