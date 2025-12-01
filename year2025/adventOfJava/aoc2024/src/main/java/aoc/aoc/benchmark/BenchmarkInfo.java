package aoc.aoc.benchmark;

public record BenchmarkInfo(String label, String description) {

    public static BenchmarkInfo of(String label, String description) {
        return new BenchmarkInfo(label, description);
    }
}
