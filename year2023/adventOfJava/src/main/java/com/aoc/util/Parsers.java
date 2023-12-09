package com.aoc.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Parsers {

    public static int[] ints(String line) {
        return ints(line, "\\s+");
    }

    public static int[] ints(String line, String regex) {
        return Arrays.stream(line.split(regex))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    public static List<Integer> intsList(String line) {
        return intsList(line, "\\s+");
    }

    public static List<Integer> intsList(String line, String regex) {
        return Arrays.stream(line.split(regex))
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList());
    }

    public static long[] longs(String line) {
        return longs(line, "\\s+");
    }

    public static long[] longs(String line, String regex) {
        return Arrays.stream(line.split(regex))
                .mapToLong(Long::parseLong)
                .toArray();
    }

    public static List<Long> longsList(String line) {
        return longsList(line, "\\s+");
    }

    public static List<Long> longsList(String line, String regex) {
        return Arrays.stream(line.split(regex))
                .mapToLong(Long::parseLong)
                .boxed()
                .collect(Collectors.toList());
    }

    public static int sumIntList(List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).sum();
    }
}
