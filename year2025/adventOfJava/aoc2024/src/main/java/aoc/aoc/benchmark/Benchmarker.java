package aoc.aoc.benchmark;

import java.util.Set;

public interface Benchmarker {

    Benchmarks getBenchmarks();

    boolean supports(String label);

    void requestNewBenchmark(String label);

    Set<BenchmarkInfo> supportedBenchmarks();

    BenchmarkRunner ifWasRequested(String label);

    interface BenchmarkRunner {

        void start();

        void end();
    }
}
