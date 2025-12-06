package aoc.aoc.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public enum Operand {
    ADD("+", '+'),
    SUB("-", '-'),
    MUL("*", '*'),
    DIV("/", '/');

    private final String str;
    private final char ch;

    private static final Map<Character, Operand> CHAR_LOOKUP = new HashMap<>();
    private static final Map<String, Operand> STRING_LOOKUP = new HashMap<>();

    static {
        for (Operand op : values()) {
            CHAR_LOOKUP.put(op.ch, op);
            STRING_LOOKUP.put(op.str, op);
        }
    }

    public static Operand fromChar(char c) {
        Operand op = CHAR_LOOKUP.get(c);
        if (op == null) {
            throw new IllegalArgumentException("Unknown operand: " + c);
        }
        return op;
    }

    public static Operand fromString(String s) {
        Operand op = STRING_LOOKUP.get(s);
        if (op == null) {
            throw new IllegalArgumentException("Unknown operand: " + s);
        }
        return op;
    }
}
