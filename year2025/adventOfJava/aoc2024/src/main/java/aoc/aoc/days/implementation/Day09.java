package aoc.aoc.days.implementation;

import aoc.aoc.cache.MemoIgnore;
import aoc.aoc.cache.Memoize;
import aoc.aoc.days.interfaces.DayStringParser;
import aoc.aoc.util.Coordinate;
import aoc.aoc.util.KDTree;
import aoc.aoc.util.Matrix;
import aoc.aoc.util.MatrixUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static aoc.aoc.util.Utils.isOdd;

public class Day09 extends DayStringParser {
    
    @Override
    protected Object part1Impl(String input) {
        var positions = parseCoordinates(input);
        var areas = calculateAreas(positions);
        
        return areas.getFirst().area();
    }

    @Override
    protected Object part2Impl(String input) {
        var positions = parseCoordinates(input);
        var edges = new HashMap<Coordinate, Coordinate>();

        var previous = positions.getLast();
        for (Coordinate current : positions) {
            edges.put(previous, current);
            previous = current;
        }
        

        var areas = calculateAreas(positions);
        int size = areas.size();
        int counter = 0;
        Area best = null;
        for (int compress = 256; compress > 0; compress>>=1) {
            doStuff(areas, counter, size, edges, compress);
        }


        return best.area;
    }

    private List<Area> doStuff(List<Area> areas, int counter, int size, Map<Coordinate, Coordinate> edges, int compress) {
        
        int index = 0;
        for(var area : areas) {
            System.out.printf("%s/%s%n", ++counter, size);
            boolean result = isAreaIsInside(edges, 
                    new Coordinate(area.from.y()/compress, area.from.x()/compress), 
                    new Coordinate(area.to.y()/compress, area.to.x()/compress));

            if (result) {
                break;
            }
                
            ++index;
        }

        var fresh = new ArrayList<Area>();
        for (int i = index; i < areas.size(); i++) {
            fresh.add(areas.get(i));
        }
        
        return fresh;
    }

    private boolean isAreaIsInside(Map<Coordinate, Coordinate> edges, Coordinate from, Coordinate to) {
//        var from = area.from;
//        var to = area.to;

        int minX = Math.min(from.x(), to.x());
        int maxX = Math.max(from.x(), to.x());
        int minY = Math.min(from.y(), to.y());
        int maxY = Math.max(from.y(), to.y());
        
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (x == minX || x == maxX || y == minY || y == maxY) {
                    boolean isInside = isInside(edges,new Coordinate(y,x));
                    if (!isInside) {
                        return false;
                    }
                }

            }
        }
        
        return true;
    }

    protected boolean isInside(Map<Coordinate, Coordinate> edges, Coordinate position) {
        if (edges.containsKey(position))
            return true;
        
        int xp = position.x();
        int yp = position.y();
        
        int count = 0;
        for (var edge : edges.entrySet()) {
            int x1 = edge.getKey().x(), y1 = edge.getKey().y();
            int x2 = edge.getValue().x(), y2 = edge.getValue().y();
            boolean isOverOrUnder = (yp < y1 && yp < y2) || (yp > y1 && yp > y2);
            boolean isRightOfBoth = xp > x1 && xp > x2;
            boolean isLeftOfBoth = xp < x1 && xp < x2;
            boolean isSameColumn = (xp == x1 && x1 == x2);
            boolean isSameRow = (yp == y1 && y1 == y2);
            boolean edgeIsHorizontal = y1 == y2;
            boolean edgeIsVertical = x1 == x2;
            
            if (edgeIsVertical && xp == x1 && between(yp,y1,y2))
                return true;
            if (edgeIsHorizontal && yp == y1 && between(xp,x1,x2))
                return true;

            if ((yp < y1) != (yp < y2) && xp < x1 + ((yp-y1)/(y2-y1))*(x2-x1))
                ++count;
        }
        
        return isOdd(count);
    }

    public static boolean between(int p, int a, int b) {
        return (p >= Math.min(a, b) && p <= Math.max(a, b));
    }

    private static int @NotNull [] arrayFromCoordinates(Coordinate from) {
        return new int[]{from.y(), from.x()};
    }

    record Node(Coordinate position, Coordinate next, boolean goingUpOrRight) {
    }

    record Area(Coordinate from, Coordinate to, long area) implements Comparable<Area> {
        @Override
        public int compareTo(Area o) {
            return Long.compareUnsigned(o.area, this.area);
        }
    }

    private static long calcArea(Coordinate from, Coordinate to) {
        return (Math.abs((long)from.x() - to.x())+1) *( Math.abs((long)from.y() - to.y())+1);
    }

    
    private static List<Area> calculateAreas(List<Coordinate> positions) {
        var list = new ArrayList<Area>();

        for (int i = 0; i < positions.size(); i++) {
            var from = positions.get(i);
            for (int j = i + 1; j < positions.size(); j++) {
                if (i == j)
                    continue;

                var to = positions.get(j);
                list.add(new Area(from, to, calcArea(from, to)));
            }
        }
        list.sort(Comparator.naturalOrder());
        return list;
    }

    private static List<Coordinate> parseCoordinates(String input) {
        return input.lines()
                .map(line -> {
                    var split = Arrays.stream(line.split(","))
                            .mapToInt(Integer::parseInt).toArray();
                    return new Coordinate(split[1], split[0]);
                })
                .toList();
    }
}
