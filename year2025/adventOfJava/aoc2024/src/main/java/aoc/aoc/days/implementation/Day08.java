package aoc.aoc.days.implementation;

import aoc.aoc.days.interfaces.DayStringParser;
import aoc.aoc.util.Graph;

import java.util.*;

public class Day08 extends DayStringParser {
    
    @Override
    protected Object part1Impl(String input) {
        return solve(input);
    }

    @Override
    protected Object part2Impl(String input) {
        return solve2(input);
    }

    private record Box(int x, int y, int z) implements Comparable<Box> {

        @Override
        public int compareTo(Box o) {
            return (int) (this.dist() - o.dist());
        }

        private long dist() {
            return (long) x * x + (long) y * y + (long) z * z;
        }

        public static double distance(Box a, Box b) {
            long dx = (long) a.x() - b.x();
            long dy = (long) a.y() - b.y();
            long dz = (long) a.z() - b.z();

            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    public record Pair<T>(T first, T second) {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair<?> other)) return false;

            return (Objects.equals(first, other.first) && Objects.equals(second, other.second)) ||
                    (Objects.equals(first, other.second) && Objects.equals(second, other.first));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(first) + Objects.hashCode(second);
        }
    }

    private long solve2(String input) {
        var boxes = parseBoxes(input);
        var graph = new Graph<Box>();

        var sorted = getPairsSorted(boxes);
        
        for (var e : sorted) {
            var from =  e.getKey().first;
            var to = e.getKey().second;

            graph.addEdge(from,to);
            graph.addEdge(to,from);
            
            if (graph.allReachableFrom(from).size() == boxes.size()) {
                return (long) from.x * to.x;
            }
        }
        
        return -1L;
    }


    private long solve(String input) {
        var boxes = parseBoxes(input);
        var graph = new Graph<Box>();

        var sorted = getPairsSorted(boxes);

        int count = isSample ? 10 : 1000;
        for (var e : sorted) {
            if (--count < 0) 
                break;
            
            var from =  e.getKey().first;
            var to = e.getKey().second;
            
            graph.addEdge(from,to);
            graph.addEdge(to,from);
        }

        var all = new HashSet<Set<Box>>();
        for (Box box : boxes) {
            var set = graph.allReachableFrom(box);
            all.add(set);
        }
        
        return all.stream()
                .map(Set::size)
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToLong(Integer::longValue)
                .reduce(1L,
                        (a,b) -> a*b
                );
    }

    private static List<Map.Entry<Pair<Box>, Double>> getPairsSorted(List<Box> boxes) {
        var closenessMap = new HashMap<Pair<Box>, Double>();
        for (Box from : boxes) {
            var entry = getClosestBox(from, boxes);
            closenessMap.putAll(entry);
        }

        return closenessMap.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .toList();
    }

    private static List<Box> parseBoxes(String input) {
        return input.lines()
                .map(line -> {
                    var split = Arrays.stream(line.split(","))
                            .mapToInt(Integer::parseInt).toArray();
                    return new Box(split[0], split[1], split[2]);
                })
                .toList();
    }
    
    private static Map<Pair<Box>, Double> getClosestBox(Box from, List<Box> list) {
        var map = new HashMap<Pair<Box>, Double>();
        for (Box to : list) {
            if (from.equals(to))
                continue;
            
            double dist = Box.distance(from, to);
            map.put(new Pair<>(from, to),dist);
        }
        
        return map;
    }
}
