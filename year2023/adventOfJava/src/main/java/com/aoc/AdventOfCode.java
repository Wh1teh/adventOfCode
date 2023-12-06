package com.aoc;

import com.aoc.days.DayInterface;
import com.aoc.util.ClassEnum;
import com.aoc.util.XD;
import com.aoc.util.Pair;

public class AdventOfCode {

    private static final int DEFAULT_ROUNDS = 100;

    public static void main(String[] args) {
        // parse args
        int day = getDay(args);
        int part = getPart(args);
        int rounds = getRounds(args);
        if (day < 0 || part < 0) {
            playAllDays((rounds <= 0 ? DEFAULT_ROUNDS : rounds));
            return;
        }

        // get the class at index provided by args
        Class<? extends DayInterface> daySolverClass = ClassEnum.getClassByIndex(day);

        // create an instance of the class
        if (daySolverClass != null) {
            try {
                DayInterface daySolver = daySolverClass.getDeclaredConstructor().newInstance();
                daySolver.setDay(day);

                System.out.println(XD.bold + "Day %02d:".formatted(day) + XD.reset);

                playPart(daySolver, part, (rounds <= 0 ? 1 : rounds));

                XD.printLights(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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

    private static int getRounds(String[] args) {
        if (args != null && args.length > 2) {
            return Integer.parseInt(args[2]);
        }
        return -1;
    }

    private static void playAllDays(int rounds) {
        for (int i = 0; i <= 25; i++) {
            playDay(i, rounds);
        }
    }

    private static void playDay(int index, int rounds) {
        Class<? extends DayInterface> daySolverClass = ClassEnum.getClassByIndex(index);
        if (daySolverClass != null) {
            try {
                DayInterface daySolver = daySolverClass.getDeclaredConstructor().newInstance();
                daySolver.setDay(index);

                System.out.println(XD.bold + "Day %02d:".formatted(index) + XD.reset);

                playPart(daySolver, 1, rounds);
                System.out.println();
                playPart(daySolver, 2, rounds);

                XD.printLights(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void playPart(DayInterface daySolver, int part) {
        playPart(daySolver, part, 1);
    }

    private static void playPart(DayInterface daySolver, int part, int rounds) {
        Pair<String, Long> result;
        result = daySolver.solve(part, rounds);
        XD.printResult(result.getKey(), part);
        XD.printTimes(result.getValue(), rounds);
    }
}