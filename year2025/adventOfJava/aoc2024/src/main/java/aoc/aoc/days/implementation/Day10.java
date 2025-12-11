package aoc.aoc.days.implementation;

import aoc.aoc.cache.MemoIgnore;
import aoc.aoc.cache.Memoize;
import aoc.aoc.days.interfaces.DayStringParser;
import aoc.aoc.util.Graph;
import aoc.aoc.util.Pair;
import aoc.aoc.util.Utils;

import java.util.*;
import java.util.function.Supplier;

public class Day10 extends DayStringParser {

    @Override
    protected Object part1Impl(String input) {
        var lightBoxes = parseLightBoxes(input);

        int total = 0;
        for (LightBox lightBox : lightBoxes) {
            total += epic1(lightBox);
        }
        
        return total;
    }
    
    protected int epic1(LightBox lightBox) {
        var map = new HashMap<Integer, Integer>();
        Arrays.stream(lightBox.instructions).forEach(i -> {
            map.put(i, 1);
        });
        
        while (!map.containsKey(lightBox.goal)) {
            var instructions = map.entrySet().stream().toList();
            int size = instructions.size();
            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size; j++) {
                    if (i == j)
                        continue;

                    var a = instructions.get(i);
                    var b = instructions.get(j);
                    int xorResult = a.getKey() ^ b.getKey();
                    map.merge(xorResult, a.getValue() + b.getValue(), Math::min);
                }
            }
        }

        return map.get(lightBox.goal);
    }


    @Override
    protected Object part2Impl(String input) {
        var lightBoxes = parseLightBoxes(input);

        long total = 0;
        for (LightBox lightBox : lightBoxes) {
            total += epic2(lightBox);
        }

        return total;
    }

    protected int epic2(LightBox lightBox) {
        var map = new HashMap<int[], Integer>();

        var instructions = lightBox.instructions;
        var joltages = lightBox.joltages;
        int smallest =Integer.MAX_VALUE;
        for (int i = 0; i < instructions.length; i++) {
            int result = dfs(instructions, i, Arrays.copyOf(joltages,joltages.length),1);
            smallest = Math.min(result,smallest);
        }

        System.out.println(smallest);
        return smallest;
    }

    Map<Long, Integer> memo = new HashMap<>();
    
    protected int dfs(int[] instructions, int index, int[] current, int depth) {
        int instruction = instructions[index];
        addBits(current,instruction);
        
        if (anyIsNegative(current)) {
            return Integer.MAX_VALUE;
        }
        
        if (allZeroes(current)) {
            return depth;
        }

        int smallest = Integer.MAX_VALUE;
        for (int i = 0; i < instructions.length; i++) {
            int got = dfs(instructions,i,Arrays.copyOf(current,current.length),depth+1);
//            subBits(current,instructions[i]);
            smallest = Math.min(smallest, got);
        }

        return smallest;
    }
    
    private static boolean allZeroes(int[] arr) {
        return Arrays.stream(arr).allMatch(n -> n==0);
    }
    
    private static boolean anyIsNegative(int[] arr) {
        return Arrays.stream(arr).anyMatch(n -> n < 0);
    }

    private static void subBits(int[] joltages, int instruction) {
        for (byte n = 0; n < Integer.SIZE && n < joltages.length; n++) {
            boolean bit = Utils.nthBit(instruction, n);
            joltages[n] += bit ? 1 : 0;
        }
    }

    private static void addBits(int[] joltages, int instruction) {
        for (byte n = 0; n < Integer.SIZE && n < joltages.length; n++) {
            boolean bit = Utils.nthBit(instruction, n);
            joltages[n] -= bit ? 1 : 0;
        }
    }

    private static boolean isExceeded(int[] joltages) {
        return Arrays.stream(joltages).anyMatch(a -> a < 0);
    }

    private static boolean isSatisfied(int[] joltages) {
        return Arrays.stream(joltages).allMatch(a -> a == 0);
    }

    private static boolean[] intToBits(int value) {
        boolean[] bits = new boolean[32];

        for (int i = 0; i < 32; i++) {
            bits[i] = ((value >>> i) & 1) == 1;
        }

        return bits;
    }
    
    protected record LightBox(int goal, int[] instructions, int[] joltages) {
    }
    
    private static void createInstructionGraph(int[] instructions, Graph<Integer> graph) {
        for (var from : instructions) {
            for (var to : instructions) {
                graph.addEdge(from, to);
            }
        }
    }

    private static List<LightBox> parseLightBoxes(String input) {
        return input.lines().map(line -> {
                    var split = line.split(" ");
                    return new LightBox(parseGoal(split), parseInstructions(split), parseJoltages(split));
                })
                .toList();
    }
    
    private static int parseGoal(String[] raw) {
//        return Integer.parseInt(raw[0]
//                .replaceAll("[\\[\\]]", "")
//                .replace('.','0')
//                .replace('#','1'),2
//        );
        
        int result = 0;
        char[] charArray = raw[0].toCharArray();
        for (int i = charArray.length - 2; i >0; i--) {
            char ch = charArray[i];
            result <<= 1;
            result += ch == '#' ? 1 : 0;
        }
        return result;
    }
    
    private static int[] parseInstructions(String[] raw) {
        int[] instructions = new int[raw.length - 2];
        for (int i = 1; i < raw.length - 1; i++) {
            instructions[i-1] = parseInstruction(raw[i]);
        }
//        instructions[0] = 0;
        return instructions;
    }
    
    private static int parseInstruction(String raw) {
        int result = 0;
        char[] charArray = raw.toCharArray();
        for (int i = 1; i < charArray.length - 1; i++) {
            char ch = charArray[i];
            if(ch==',')
                continue;
            result += 1 << (ch - '0');
        }
        return result;
    }

    private static int[] parseJoltages(String[] raw) {
        var str = raw[raw.length-1];
        var split = str.substring(1, str.length()-1).split(",");
        return Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
    }
}
