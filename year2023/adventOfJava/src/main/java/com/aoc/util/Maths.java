package com.aoc.util;

public class Maths {

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

}
