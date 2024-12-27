package aoc.aoc.solver;

import aoc.aoc.days.enums.Part;

public interface Solver<T> {

    T with(Part part);
}
