package aoc.aoc.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class Quad<A, B, C, D> {

    private A first;
    private B second;
    private C third;
    private D fourth;
}
