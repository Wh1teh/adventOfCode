package aoc.aoc.solver;

import aoc.aoc.days.Part;

public abstract class AbstractSolver<T extends Solver<T>> implements Solver<T> {

    protected Part part;

    @SuppressWarnings("unchecked")
    @Override
    public T with(Part part) {
        this.part = part;
        return (T) this;
    }
}
