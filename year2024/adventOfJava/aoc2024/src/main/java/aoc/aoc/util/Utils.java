package aoc.aoc.util;

public class Utils {

    private Utils() {
    }

    public static String replaceChartAt(String line, int x, char with) {
        return "%s%c%s".formatted(
                line.substring(0, x),
                with,
                line.substring(x + 1)
        );
    }
}
