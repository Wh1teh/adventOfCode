package aoc.aoc.days;

public interface Day extends Cloneable {

    String part1(String input);

    String part2(String input);

    String debugString();

    void clearDebug();
}
