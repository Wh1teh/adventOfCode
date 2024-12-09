package aoc.aoc.days;

import lombok.RequiredArgsConstructor;

import java.util.*;

import static aoc.aoc.util.Utils.isEven;

public class Day09 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + calculateChecksum(input);
    }

    @Override
    protected String part2Impl(String input) {
        return "" + calculateChecksum2(input);
    }

    private static int[] toIntArray(String input) {
        return input.strip().chars().map(c -> c - '0').toArray();
    }

    private static int calculateChecksum(String input) {
        var disk = buildDisk(toIntArray(input));

        defragPart1(disk);

        return getChecksum1(disk);
    }

    private static ArrayList<Integer> buildDisk(int[] numbers) {
        var disk = new ArrayList<Integer>();

        for (int i = 0; i < numbers.length; i++) {
            int n = numbers[i];
            int id = i / 2;
            for (int spaceNeeded = 0; spaceNeeded < n; spaceNeeded++) {
                disk.add(isEven(i) ? id : null);
            }
        }

        return disk;
    }

    private static void defragPart1(List<Integer> disk) {
        int right = disk.size() - 1;
        int left = 0;
        while (left < right) {
            if (disk.get(left) == null) {
                while (disk.get(right) == null) {
                    right--;
                }
                Collections.swap(disk, left, right);
            }
            left++;
        }
    }

    private static int getChecksum1(List<Integer> disk) {
        int checksum = 0;

        for (int i = 0; i < disk.size(); i++) {
            checksum += disk.get(i) == null ? 0 : disk.get(i) * i;
        }

        return checksum;
    }

    private static long calculateChecksum2(String input) {
        var disk = buildDisk2(toIntArray(input));
        return getChecksum2(defragPart2(disk));
    }

    private static Disk buildDisk2(int[] array) {
        var numbers = new ArrayList<Span>();
        var emptys = new ArrayList<Span>();

        for (int i = 0; i < array.length; i++) {
            int size = array[i];
            int id = i / 2;
            if (isEven(i))
                numbers.add(new NumberSpan(id, size));
            else
                emptys.add(new EmptySpan(size));
        }

        return new Disk(numbers, emptys);
    }

    private static List<Span> defragPart2(Disk disk) {
        return collectToOneSpan(
                moveFromRightToEmpty(disk)
        );
    }

    private static Disk moveFromRightToEmpty(Disk disk) {
        var numbers = disk.numbers;
        for (int index = numbers.size() - 1; index >= 0; index--) {
            var numberSpan = numbers.get(index);
            for (int left = 0; left < index; left++) {
                var emptySpan = disk.emptys().get(left);

                if (emptySpan.addIfPossible(numberSpan)) {
                    numbers.set(index, new EmptySpan(numberSpan.size()));
                    break;
                }
            }
        }

        return disk;
    }

    private static ArrayList<Span> collectToOneSpan(Disk disk) {
        var defrag = new ArrayList<Span>();

        var numbers = disk.numbers();
        var emptys = disk.emptys();
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) == null)
                continue;

            defrag.add(numbers.get(i));
            if (i < emptys.size())
                defrag.add(emptys.get(i));
        }

        return defrag;
    }

    private static long getChecksum2(List<Span> disk) {
        long checksum = 0;

        int index = 0;
        for (var span : disk) {
            while (span.hasNext()) {
                var next = span.next();
                checksum += (long) next * index++;
            }
        }

        return checksum;
    }

    private record Disk(List<Span> numbers, List<Span> emptys) {
    }

    private interface Span {

        int size();

        boolean hasNext();

        int next();

        boolean addIfPossible(Span span);
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

        @Override
        public boolean addIfPossible(Span span) {
            return false;
        }
    }

    @RequiredArgsConstructor
    private static class EmptySpan implements Span {

        private final int[] numbers;
        private int traverlsalIndex = 0;
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
            return traverlsalIndex < numbers.length;
        }

        @Override
        public int next() {
            return numbers[traverlsalIndex++];
        }

        public boolean addIfPossible(Span numberSpan) {
            if (numberSpan.size() > size())
                return false;

            while (numberSpan.hasNext()) {
                numbers[additionIndex++] = numberSpan.next();
            }

            return true;
        }
    }
}
