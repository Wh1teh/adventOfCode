package com.aoc.days;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.aoc.util.Pair;
import com.aoc.util.ProgressBar;
import com.aoc.util.ReadFile;
import com.aoc.util.XD;

public class Day implements DayInterface {

    protected int DAY;
    protected List<String> LINES = new ArrayList<>();

    @Override
    public void setDay(int day) {
        DAY = day;
        loadFile();
    }

    @Override
    public void loadFile() {
        LINES = ReadFile.getData(
                "src/main/java/com/aoc/data/day%02d".formatted(DAY) + ".txt");

        if (LINES == null || LINES.size() == 0)
            LINES = null;
    }

    @Override
    public void loadSample() {
        LINES = ReadFile.getData(
                "src/main/java/com/aoc/data/day%02d".formatted(DAY) + "_sample" + ".txt");

        if (LINES == null || LINES.size() == 0)
            LINES = null;
    }

    @Override
    public Pair<String, Long> solve(int part) {
        return solve(part, 1);
    }

    @Override
    public Pair<String, Long> solve(int part, int rounds) {
        if (LINES == null || LINES.size() == 0)
            return new Pair<String, Long>("Day %02d is not available".formatted(DAY), -1L);

        String result = "";

        System.out.print("part %s:\t".formatted(part));

        ProgressBar progress = new ProgressBar(8, 50);
        progress.initProgressBar();
        int progressMilestone = Math.max(1, rounds / progress.getLen());

        Long averageTime = 0L;
        for (int i = 0; i < rounds; i++) {
            Instant startTime = Instant.now();

            result = switch (part) {
                case 0 -> solveSample();
                case 1 -> solveFirstPart();
                case 2 -> solveSecondPart();
                default -> throw new IllegalArgumentException("Unexpected value: " + part);
            };

            Instant endTime = Instant.now();
            averageTime += Duration.between(startTime, endTime).toNanos();

            if (i % progressMilestone == 0)
                progress.progress((double) (i + 1) / rounds);
        }
        averageTime /= rounds;

        System.out.print(XD.clearln);

        if (result == null || result.isEmpty())
            return new Pair<String, Long>("Day %02d part %d is Unimplemented".formatted(DAY, part), -1L);

        return new Pair<String, Long>(result, averageTime);
    }

    @Override
    public String solveSample() {
        loadSample();

        StringBuilder result = new StringBuilder();

        result.append(solveFirstPart());
        result.append("\n---\n");
        result.append(solveSecondPart());

        return result.toString();
    }

    @Override
    public String solveFirstPart() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'solveFirstPart'");
    }

    @Override
    public String solveSecondPart() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'solveSecondPart'");
    }

}
