package com.aoc.util;

public class NumberLazy extends Number {
    private Number number;

    public NumberLazy(Number number) {
        this.number = number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public int intValue() {
        return this.number.intValue();
    }

    @Override
    public long longValue() {
        return this.number.longValue();
    }

    @Override
    public float floatValue() {
        return this.number.floatValue();
    }

    @Override
    public double doubleValue() {
        return this.number.doubleValue();
    }

}
