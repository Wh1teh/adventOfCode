package com.aoc.util;

import java.util.List;

public class Maths {

    @FunctionalInterface
    interface Operand {
        double apply(double value);
    }

    public static class MultiplierOperand implements Operand {
        private int multiplier;
    
        public MultiplierOperand() {
            this.multiplier = 1;
        }
    
        @Override
        public double apply(double value) {
            double result = value * multiplier;
            multiplier++;
            return result;
        }
    }

    public static long GCD(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    @SafeVarargs
    public static <T extends Number> long LCM(T... nums) {
        if (nums == null || nums.length < 1) {
            throw new IllegalArgumentException("Input array is empty or null.");
        }

        if (nums.length == 1) {
            return nums[0].longValue();
        }

        long temp = nums[0].longValue();
        for (int i = 1; i < nums.length; i++) {
            temp = LCMofTwo(temp, nums[i].longValue());
        }
        return temp;
    }

    private static long LCMofTwo(long a, long b) {
        return Math.abs(a * b) / GCD(a, b);
    }

    public static <T extends Number> double sumArray(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array is null or empty");
        }

        double sum = 0.0;
        for (T element : array) {
            sum += element.doubleValue();
        }

        return sum;
    }

    public static <T extends Number> double sumList(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List is null or empty");
        }

        double sum = 0.0;
        for (T element : list) {
            sum += element.doubleValue();
        }

        return sum;
    }

    public static <T extends Number> double sumList(List<T> list, Operand operand) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List is null or empty");
        }

        double sum = 0.0;
        for (T element : list) {
            sum += operand.apply(element.doubleValue());
        }

        return sum;
    }

    

}
