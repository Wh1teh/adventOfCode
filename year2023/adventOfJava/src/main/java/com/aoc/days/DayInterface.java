package com.aoc.days;

import java.util.List;

public interface DayInterface {

    String solve(int part);
    StringBuilder solveSample(List<String> lines);
    StringBuilder solveFirstPart(List<String> lines);
    StringBuilder solveSecondPart(List<String> lines);
}
