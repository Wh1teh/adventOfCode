package com.aoc.util;

public class XD {

    // ANSI color and style codes
    public static String clearln = "\033[2K\r";
    public static String bold = "\u001B[1m";
    public static String reset = "\u001B[0m";
    public static String resetBold = "\u001B[22m";
    public static String resetColor = "\u001B[39m";

    // colors with bold
    public static String red = "\u001B[1;31m";
    public static String green = "\u001B[1;32m";
    public static String yellow = "\u001B[1;33m";
    public static String blue = "\u001B[1;34m";
    public static String magenta = "\u001B[1;35m";
    public static String cyan = "\u001B[1;36m";
    public static String gray = "\u001B[1;30m";

    // colors without bold
    public static String grayL = "\u001B[30m";

    public static String style(Number num, String color) {
        return style(String.valueOf(num), color);
    }

    public static String style(String text, String color) {
        return color + text + reset;
    }

    public static void printLights() {
        printLights(32);
    }

    public static void printLights(int len) {
        for (int j = 0; j <= len; j++)
            System.out.print((j % 2 == 1 ? yellow : "") + "-" + (j % 2 == 1 ? reset : ""));
        System.out.println();
    }

    public static void printResult(String result, int part) {
        if (result != null && result.contains("\n"))
            result = padMultiLine(result, 1);

        System.out.println("part %d:\t%s"
                .formatted(
                        part,
                        style(result, green)));
    }

    public static void printTimes(Long nanos) {
        printTimes(nanos, -1);
    }

    public static void printTimes(Long nanos, int rounds) {
        System.out.println("took:\t[%s%s] [%s%s] [%s%s] %s"
                .formatted(
                        style(nanos / 1000000, red), style("ms", bold),
                        style(nanos / 1000, red), style("µs", bold),
                        style(nanos, red), style("ns", bold),
                        rounds > 0 ? style("(avg of " + rounds + ")", grayL) : ""));
    }

    public static String padMultiLine(String result, int padding) {
        if (result == null || padding < 0)
            throw new IllegalArgumentException("Invalid input");

        String[] lines = result.split("\n");

        if (lines.length <= 1)
            return result;

        StringBuilder paddedResult = new StringBuilder(lines[0]);

        for (int i = 1; i < lines.length; i++) {
            // add a newline
            if (i < lines.length)
                paddedResult.append('\n');

            // add padding to each line after the first one
            for (int j = 0; j < padding; j++)
                paddedResult.append('\t');

            paddedResult.append(lines[i]);
        }

        return paddedResult.toString();
    }

    public static void printAt(String str, int at) {
        // ANSI escape code to move the cursor to a specific column
        String moveToColumn = "\u001B[" + (at + 1) + "G";
        System.out.print(moveToColumn + str);
    }

}
