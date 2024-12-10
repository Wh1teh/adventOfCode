package aoc.aoc.util;

import java.util.List;

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
    public void iterate(MatrixConsumer<Character> consumer) {
        for (int y = 0; y < size(); y++) {
            for (int x = 0; x < size(); x++) {
                consumer.accept(get(y, x), y, x);
            }
        }
    }

    @Override
    public int size() {
        return matrix.size();
    }
}
