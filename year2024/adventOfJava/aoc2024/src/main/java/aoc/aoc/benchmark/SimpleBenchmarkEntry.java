package aoc.aoc.benchmark;

import lombok.experimental.StandardException;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public class SimpleBenchmarkEntry implements BenchmarkEntry {

    private Instant start;
    private Instant end;

    private final AtomicLong operations = new AtomicLong(0L);

    @Override
    public void start() {
        this.start = Instant.now();
    }

    public void end() {
        if (end != null)
            throw new BenchmarkException("End already triggered");
        end = Instant.now();
    }

    @Override
    public Duration duration() {
        return Duration.between(start, end);
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
