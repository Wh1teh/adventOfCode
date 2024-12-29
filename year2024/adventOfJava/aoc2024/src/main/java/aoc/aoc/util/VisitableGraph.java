package aoc.aoc.util;

import java.util.function.Consumer;
import java.util.function.Function;

public interface VisitableGraph<T> {

    <R> R acceptTraversalMethod(Function<VisitableGraph<T>, R> function);

    void forAdjacent(T current, Consumer<T> consumer);
}
