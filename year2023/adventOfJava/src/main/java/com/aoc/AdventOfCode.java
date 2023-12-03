package com.aoc;

import com.aoc.days.DayInterface;
import com.aoc.util.ClassEnum;
import com.aoc.util.GenerateDays;

public class AdventOfCode {

    private static int getDay(String[] args) {
        if (args != null && args.length > 0) {
            return Integer.parseInt(args[0]);
        }
        return -1;
    }

    private static int getPart(String[] args) {
        if (args != null && args.length > 1) {
            return Integer.parseInt(args[1]);
        }
        return -1;
    }

    public static void main(String[] args) {
        // GenerateDays.generate("src/main/java/com/aoc/", "com.aoc.");

        // parse args
        int day = getDay(args);
        int part = getPart(args);
        if (day < 0 || part < 0) {
            System.out.println(
                    "day:%d or part:%d not provided, playing each day...".formatted(day, part));

            for (int i = 0; i <= 25; i++) {
                Class<? extends DayInterface> daySolverClass = ClassEnum.getClassByIndex(i);
                if (daySolverClass != null) {
                    try {
                        DayInterface daySolver = daySolverClass.getDeclaredConstructor().newInstance();
                        System.out.println("Day% 02d:".formatted(i));
                        System.out.println(daySolver.solve(1));
                        System.out.println("---");
                        System.out.println(daySolver.solve(2));
                        System.out.println();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return;
        }

        // get the class at index provided by args
        Class<? extends DayInterface> daySolverClass = ClassEnum.getClassByIndex(day);

        // create an instance of the class
        if (daySolverClass != null) {
            try {
                DayInterface daySolver = daySolverClass.getDeclaredConstructor().newInstance();

                // call the solve method
                System.out.println(daySolver.solve(part));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}