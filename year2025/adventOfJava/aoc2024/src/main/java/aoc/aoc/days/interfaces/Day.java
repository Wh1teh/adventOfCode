package aoc.aoc.days.interfaces;

public interface Day<T> extends Cloneable {

    String part1(T input);

    String part2(T input);

    String debugString();

    void clearDebug();
}
