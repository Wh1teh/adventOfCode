package aoc.aoc.util;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
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

    public static <T> void forEachWithExecutorService(Iterable<T> iterable, Consumer<T> functionToSubmit) {
        withExecutorService(e ->
                iterable.forEach(t ->
                        e.execute(() -> functionToSubmit.accept(t))
                )
        );
    }

    public static boolean isEven(int number) {
        return (number & 1) == 0;
    }

    public static String toStringStripBracketsAndWhiteSpace(Object object) {
        return object.toString()
                .replaceAll("[\\[\\]\\s]", "");
    }

    public static <T> List<T> listAdd(List<T> list, T element) {
        list.add(element);
        return list;
    }

    public static <T> void trailingIteration(Iterable<T> iterable, BiConsumer<T, T> consumer) {
        var iterator = iterable.iterator();
        if (!iterator.hasNext())
            return;

        T previous = iterator.next();
        while (iterator.hasNext()) {
            T next = iterator.next();
            consumer.accept(previous, next);
            previous = next;
        }
    }
}
