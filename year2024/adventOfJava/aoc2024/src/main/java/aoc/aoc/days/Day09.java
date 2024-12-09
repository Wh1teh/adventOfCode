package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static aoc.aoc.days.Part.PART_1;
import static aoc.aoc.days.Part.PART_2;
import static aoc.aoc.util.Utils.isEven;

public class Day09 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + new Disk(toIntArray(input))
                .with(PART_1)
                .defragment()
                .calculateChecksum();
    }

    @Override
    protected String part2Impl(String input) {
        return "" + new Disk(toIntArray(input))
                .with(PART_2)
                .defragment()
                .calculateChecksum();
    }

    private static int[] toIntArray(String input) {
        return input.strip().chars().map(c -> c - '0').toArray();
    }

    private static class Disk extends AbstractSolver<Disk> {

        private final List<Span> numbers = new ArrayList<>();
        private final List<Span> emptys = new ArrayList<>();

        public Disk(int[] array) {
            initDisk(array);
        }

        private void initDisk(int[] array) {
            for (int i = 0; i < array.length; i++) {
                int size = array[i];
                int id = i / 2;
                if (isEven(i))
                    numbers.add(new NumberSpan(id, size));
                else
                    emptys.add(new EmptySpan(size));
            }
        }

        public long calculateChecksum() {
            long checksum = 0;

            var index = new AtomicInteger();
            for (int i = 0; i < numbers.size(); i++) {
                checksum += calculateChecksumForSpan(numbers.get(i), index);
                if (i < emptys.size())
                    checksum += calculateChecksumForSpan(emptys.get(i), index);
            }

            return checksum;
        }

        private long calculateChecksumForSpan(Span span, AtomicInteger index) {
            long checksum = 0;

            while (span.hasNext()) {
                var next = span.next();
                checksum += (long) next * index.getAndIncrement();
            }

            return checksum;
        }

        public Disk defragment() {
            var lastOccurrence = new HashMap<Integer, Integer>();
            for (int index = numbers.size() - 1; index >= 0; index--) {
                if (part == PART_2)
                    moveChunkFromRightToEmpty(index, lastOccurrence);
                else
                    moveNumbersFromRightToEmpty(index, lastOccurrence);
            }

            return this;
        }

        private void moveNumbersFromRightToEmpty(int index, HashMap<Integer, Integer> lastOccurrence) {
            var numberSpan = numbers.get(index);

            int left = lastOccurrence.getOrDefault(1, 0);
            while (left < index && numberSpan.hasNext()) {
                emptys.get(left++).addAsMuchAsPossible(numberSpan);
            }

            lastOccurrence.put(1, left - 1);
        }

        private void moveChunkFromRightToEmpty(int index, HashMap<Integer, Integer> lastOccurrence) {
            var numberSpan = numbers.get(index);
            int neededSize = numberSpan.size();

            for (int left = lastOccurrence.getOrDefault(neededSize, 0); left < index; left++) {
                var emptySpan = emptys.get(left);

                if (emptySpan.addIfPossible(numberSpan)) {
                    lastOccurrence.put(neededSize, left);
                    numbers.set(index, new EmptySpan(neededSize));
                    break;
                }
            }
        }
    }

    private interface Span {

        int size();

        boolean hasNext();

        int next();

        default boolean addIfPossible(Span span) {
            return false;
        }

        default void addAsMuchAsPossible(Span span) {
        }
    }

    private static class NumberSpan implements Span {

        private final int number;
        private final int size;
        private int index = 0;

        public NumberSpan(int number, int size) {
            this.number = number;
            this.size = size;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public int next() {
            assert index < size;
            index++;
            return number;
        }
    }

    @RequiredArgsConstructor
    private static class EmptySpan implements Span {

        private final int[] numbers;
        private int traversalIndex = 0;
        private int additionIndex = 0;

        public EmptySpan(int size) {
            this.numbers = new int[size];
        }

        @Override
        public int size() {
            return numbers.length - additionIndex;
        }

        @Override
        public boolean hasNext() {
            return traversalIndex < numbers.length;
        }

        @Override
        public int next() {
            return numbers[traversalIndex++];
        }

        @Override
        public boolean addIfPossible(Span numberSpan) {
            if (numberSpan.size() > size())
                return false;

            while (numberSpan.hasNext()) {
                numbers[additionIndex++] = numberSpan.next();
            }

            return true;
        }

        @Override
        public void addAsMuchAsPossible(Span numberSpan) {
            while (numberSpan.hasNext() && size() != 0) {
                numbers[additionIndex++] = numberSpan.next();
            }
        }
    }
}
