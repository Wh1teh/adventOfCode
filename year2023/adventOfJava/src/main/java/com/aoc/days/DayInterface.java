package com.aoc.days;

import com.aoc.util.Pair;

public interface DayInterface {

    void setDay(int day);

    void loadSample();
    void loadFile();
    Pair<String, Long> solve(int part);
    Pair<String, Long> solve(int part, int rounds);
    String solveSample();
    String solveFirstPart();
    String solveSecondPart();
}
