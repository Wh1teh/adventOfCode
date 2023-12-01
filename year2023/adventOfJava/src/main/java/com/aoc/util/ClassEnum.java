package com.aoc.util;

import com.aoc.days.Day00;
import com.aoc.days.Day01;
import com.aoc.days.Day02;
import com.aoc.days.Day03;
import com.aoc.days.Day04;
import com.aoc.days.Day05;
import com.aoc.days.Day06;
import com.aoc.days.Day07;
import com.aoc.days.Day08;
import com.aoc.days.Day09;
import com.aoc.days.Day10;
import com.aoc.days.Day11;
import com.aoc.days.Day12;
import com.aoc.days.Day13;
import com.aoc.days.Day14;
import com.aoc.days.Day15;
import com.aoc.days.Day16;
import com.aoc.days.Day17;
import com.aoc.days.Day18;
import com.aoc.days.Day19;
import com.aoc.days.Day20;
import com.aoc.days.Day21;
import com.aoc.days.Day22;
import com.aoc.days.Day23;
import com.aoc.days.Day24;
import com.aoc.days.Day25;
import com.aoc.days.DayInterface;

public enum ClassEnum {
    
    DAY00(Day00.class),
    DAY01(Day01.class),
    DAY02(Day02.class),
    DAY03(Day03.class),
    DAY04(Day04.class),
    DAY05(Day05.class),
    DAY06(Day06.class),
    DAY07(Day07.class),
    DAY08(Day08.class),
    DAY09(Day09.class),
    DAY10(Day10.class),
    DAY11(Day11.class),
    DAY12(Day12.class),
    DAY13(Day13.class),
    DAY14(Day14.class),
    DAY15(Day15.class),
    DAY16(Day16.class),
    DAY17(Day17.class),
    DAY18(Day18.class),
    DAY19(Day19.class),
    DAY20(Day20.class),
    DAY21(Day21.class),
    DAY22(Day22.class),
    DAY23(Day23.class),
    DAY24(Day24.class),
    DAY25(Day25.class),
    ;

    private final Class<? extends DayInterface> clazz;

    ClassEnum(Class<? extends DayInterface> clazz) {
        this.clazz = clazz;
    }

    public DayInterface createInstance() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get the class by index
    public static Class<? extends DayInterface> getClassByIndex(int index) {
        if (index >= 0 && index < values().length) {
            return values()[index].clazz;
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for ClassEnum.");
        }
    }
}
