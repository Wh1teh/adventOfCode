package aoc.aoc.util;

import java.util.List;
import java.util.function.BiConsumer;

public class StringMatrix implements Matrix<Character> {

    private final List<String> matrix;

    public StringMatrix(String input) {
        this.matrix = input.lines().toList();
    }

    public static StringMatrix matrix(String input) {
        return new StringMatrix(input);
    }

    @Override
    public Character get(int y, int x) {
        return matrix.get(y).charAt(x);
    }

    @Override
    public Character get(Coordinate position) {
        return get(position.y(), position.x());
    }

    @Override
    public Character set(Coordinate position, Character element) {
        return set(position.y(), position.x(), element);
    }

    @Override
    public Character set(int y, int x, Character element) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    @Override
    public void iterate(MatrixConsumer<Character> consumer) {
        for (int y = 0; y < width(); y++) {
            for (int x = 0; x < width(); x++) {
                consumer.accept(get(y, x), y, x);
            }
        }
    }

    @Override
    public void iterate(BiConsumer<Character, Coordinate> consumer) {
        for (int y = 0; y < width(); y++) {
            for (int x = 0; x < width(); x++) {
                consumer.accept(get(y, x), new Coordinate(y, x));
            }
        }
    }

    @Override
    public int width() {
        return matrix.size();
    }

    @Override
    public int height() {
        return matrix.size();
    }
}
