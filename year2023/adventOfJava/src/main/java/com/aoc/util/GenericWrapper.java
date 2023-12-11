package com.aoc.util;

public class GenericWrapper<T, E> {
    T first;
    E second;

    public GenericWrapper(T t, E e) {
        this.first = t;
        this.second = e;
    }

    public T getFirst() {
        return this.first;
    }

    public E getSecond() {
        return this.second;
    }
}
