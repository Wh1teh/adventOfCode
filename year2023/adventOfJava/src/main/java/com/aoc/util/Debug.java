package com.aoc.util;

import java.util.List;

public class Debug {

    public static <T> void printList(List<T> list) {
        for (T t : list) {
            System.out.print(t.toString() + ", ");
        }
        System.out.println();
    }

    public static <T> void printArray(T[] arr) {
        for (T t : arr) {
            System.out.print(t.toString() + ", ");
        }
        System.out.println();
    }
}
