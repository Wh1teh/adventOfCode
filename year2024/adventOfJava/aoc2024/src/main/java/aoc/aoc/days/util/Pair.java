package aoc.aoc.days.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@AllArgsConstructor
public class Pair<T, E> {

    private T first;
    private E second;
}
