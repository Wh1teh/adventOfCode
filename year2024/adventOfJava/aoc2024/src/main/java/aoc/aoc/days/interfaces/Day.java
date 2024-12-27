package aoc.aoc.days.interfaces;

public interface Day extends Cloneable {

    String part1(String input);

    String part2(String input);

    String debugString();

    void clearDebug();
}
