package aoc.aoc.benchmark;

import java.util.Collections;
import java.util.Set;

public class StubBenchmarker implements Benchmarker {
    @Override
    public Benchmarks getBenchmarks() {
        return new Benchmarks(Collections.emptyMap());
    }

    @Override
    public boolean supports(String label) {
        return false;
    }

    @Override
    public void requestNewBenchmark(String label) {
        // Stub
    }

    @Override
    public Set<BenchmarkInfo> supportedBenchmarks() {
        return Set.of();
    }

    @Override
    public BenchmarkRunner ifWasRequested(String label) {
        return new BenchmarkRunner() {
            @Override
            public void start() {
                // Stub
            }

            @Override
            public void end() {
                // Stub
            }
        };
    }
}
