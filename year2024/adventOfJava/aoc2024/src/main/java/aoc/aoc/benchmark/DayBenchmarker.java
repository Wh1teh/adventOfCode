package aoc.aoc.benchmark;

import lombok.experimental.StandardException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DayBenchmarker implements Benchmarker {

    public static final String FILES = "Files";
    public static final String IMPLEMENTATION = "Implementation";

    private static final Set<BenchmarkInfo> SUPPORTED_BENCHMARKS = Set.of(
            BenchmarkInfo.of(FILES, "Reading files"),
            BenchmarkInfo.of(IMPLEMENTATION, "Running implementation")
    );
    private static final Map<String, BenchmarkInfo> AVAILABLE_BENCHMARKS = SUPPORTED_BENCHMARKS.stream()
            .collect(Collectors.toMap(BenchmarkInfo::label, info -> info));

    private final Map<BenchmarkInfo, SimpleBenchmarkEntry> benchmarks = new ConcurrentHashMap<>();

    @Override
    public Benchmarks getBenchmarks() {
        return new Benchmarks(Collections.unmodifiableMap(benchmarks));
    }

    @Override
    public boolean supports(String label) {
        return AVAILABLE_BENCHMARKS.containsKey(label);
    }

    @SuppressWarnings("java:S117")
    @Override
    public void requestNewBenchmark(String label) {
        if (!supports(label))
            throw new BenchmarkerException("Unsupported label");

        benchmarks.computeIfAbsent(AVAILABLE_BENCHMARKS.get(label), __ -> new SimpleBenchmarkEntry());
    }

    @Override
    public Set<BenchmarkInfo> supportedBenchmarks() {
        return Set.of();
    }

    @Override
    public BenchmarkRunner ifWasRequested(String label) {
        return benchmarkRunner(benchmarks.get(AVAILABLE_BENCHMARKS.get(label)));
    }

    private BenchmarkRunner benchmarkRunner(SimpleBenchmarkEntry simpleBenchmarkEntry) {
        final boolean requested = simpleBenchmarkEntry != null;
        return new BenchmarkRunner() {

            @Override
            public void start() {
                if (!requested)
                    return;

                simpleBenchmarkEntry.start();
            }

            @Override
            public void end() {
                if (!requested)
                    return;

                simpleBenchmarkEntry.end();
            }
        };
    }

    @StandardException
    static class BenchmarkerException extends RuntimeException {
    }
}
