package aoc.aoc.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class GenericMatrix<T> implements Matrix<T> {

    private final T[][] matrix;
    private final int height;
    private final int width;

    public GenericMatrix(T[][] matrix, int height, int width) {
        this.matrix = matrix;
        this.height = height;
        this.width = width;
    }

    public GenericMatrix(List<List<T>> rows) {
        this(rows, rows.size(), rows.getFirst().size());
    }

    public GenericMatrix(List<List<T>> rows, int height, int width) {
        this.height = height;
        this.width = width;

        @SuppressWarnings("unchecked")
        T[][] arr = (T[][]) Array.newInstance(
                rows.getFirst().getFirst().getClass(),
                height,
                width
        );

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                arr[y][x] = rows.get(y).get(x);
            }
        }

        this.matrix = arr;
    }

    public GenericMatrix(Supplier<T> initialValues, int widthAndHeight) {
        this(initialValues, widthAndHeight, widthAndHeight);
    }

    public GenericMatrix(Supplier<T> initialValues, int height, int width) {
        this.height = height;
        this.width = width;

        matrix = createMatrix(initialValues, height, width);
    }

    @SuppressWarnings("unchecked")
    private static <T> T[][] createMatrix(Supplier<T> initialValues, int height, int width) {
        var arr = (T[][]) Array.newInstance(initialValues.get().getClass(), height, width);
        for (int y = 0; y < height; y++) {
            var horizontal = arr[y];
            for (int x = 0; x < width; x++) {
                horizontal[x] = initialValues.get();
            }
            arr[y] = horizontal;
        }
        return arr;
    }

    public static GenericMatrix<Character> charMatrix(String input) {
        Character[][] arr = input.lines()
                .map(line -> {
                    var row = new Character[line.length()];
                    for (int i = 0; i < line.length(); i++) {
                        row[i] = line.charAt(i);
                    }
                    return row;
                })
                .toArray(Character[][]::new);

        return new GenericMatrix<>(arr, arr.length, arr[0].length);
    }

    public Stream<T[]> rows() {
        return Arrays.stream(matrix);
    }

    @Override
    public T get(int y, int x) {
        return matrix[y][x];
    }

    @Override
    public T get(Coordinate position) {
        return matrix[position.y()][position.x()];
    }

    @Override
    public void set(Coordinate position, T element) {
        set(position.y(), position.x(), element);
    }

    @Override
    public void set(int y, int x, T element) {
        matrix[y][x] = element;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public GenericMatrix<T> rotate90AntiClockwise() {
        @SuppressWarnings("unchecked")
        T[][] rotated = (T[][]) Array.newInstance(
                matrix.getClass().getComponentType().getComponentType(),
                width, height
        );

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotated[width - 1 - x][y] = matrix[y][x];
            }
        }

        return new GenericMatrix<>(rotated, width, height);
    }

    private T setPosition(Coordinate position, T element) {
        return setPosition(position.y(), position.x(), element);
    }

    private T setPosition(int y, int x, T element) {
        var previous = matrix[y][x];
        matrix[y][x] = element;
        return previous;
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
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }
}
