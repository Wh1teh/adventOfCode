package aoc.aoc.benchmark;

import java.time.Duration;

public interface BenchmarkEntry {

    void start();

    void end();

    Duration duration();

    void incrementOperations();

    long operationsCount();
}
