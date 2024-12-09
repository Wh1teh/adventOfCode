package aoc.aoc.days;

public interface Day {

    String sample(int part);

    String part(int part);

    String debugString();

    void clearDebug();
}
