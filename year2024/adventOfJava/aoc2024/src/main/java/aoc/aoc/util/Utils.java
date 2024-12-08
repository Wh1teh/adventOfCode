package aoc.aoc.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

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

    public static ExecutorService executorService() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public static void withExecutorService(Consumer<ExecutorService> function) {
        try (var executor = executorService()) {
            function.accept(executor);
        }
    }
}
