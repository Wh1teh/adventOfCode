package aoc.aoc.days;

import java.io.IOException;

public interface Day extends Cloneable {

    String sample(int part) throws IOException;

    String part(int part) throws IOException;

    String debugString();

    void clearDebug();
}
