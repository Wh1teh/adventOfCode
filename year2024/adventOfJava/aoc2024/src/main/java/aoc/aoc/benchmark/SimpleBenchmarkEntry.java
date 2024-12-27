package aoc.aoc.benchmark;

import lombok.experimental.StandardException;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicLong;

public class SimpleBenchmarkEntry implements BenchmarkEntry {

    private Long start;
    private Long end;

    private final AtomicLong operations = new AtomicLong(0L);

    @Override
    public void start() {
        this.start = System.nanoTime();
    }

    public void end() {
        if (end != null)
            throw new BenchmarkException("End already triggered");
        end = System.nanoTime();
    }

    @Override
    public Duration duration() {
        return Duration.of(end - start, ChronoUnit.NANOS);
    }

    public void incrementOperations() {
        operations.incrementAndGet();
    }

    @Override
    public long operationsCount() {
        return operations.get();
    }

    @StandardException
    public static class BenchmarkException extends RuntimeException {
    }
}
