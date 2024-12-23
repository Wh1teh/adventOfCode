package aoc.aoc.days;

import java.io.IOException;

public interface Day extends Cloneable {

    DayResult sample(int part) throws IOException;

    DayResult part(int part) throws IOException;

    String debugString();

    void clearDebug();
}
