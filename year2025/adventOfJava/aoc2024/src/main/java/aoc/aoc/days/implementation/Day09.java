package aoc.aoc.days.implementation;

import aoc.aoc.cache.MemoIgnore;
import aoc.aoc.cache.Memoize;
import aoc.aoc.days.interfaces.DayStringParser;
import aoc.aoc.util.Coordinate;
import aoc.aoc.util.Matrix;
import aoc.aoc.util.MatrixUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;

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
        var areas = calculateAreas(positions);

        var godPolygon = new java.awt.Polygon();
        for (Coordinate position : positions) {
            godPolygon.addPoint(position.x(),position.y());
        }
        var polyArea = new java.awt.geom.Area(godPolygon);
        for (Area area : areas) {
            var quad = new java.awt.Polygon();
            int xMin = Math.min(area.from.x(), area.to.x());
            int xMax = Math.max(area.from.x(), area.to.x());
            int yMin = Math.min(area.from.y(), area.to.y());
            int yMax = Math.max(area.from.y(), area.to.y());
            quad.addPoint(xMin, yMin);
            quad.addPoint(xMax, yMin);
            quad.addPoint(xMax, yMax);
            quad.addPoint(xMin, yMax);
            
            var quadArea = new java.awt.geom.Area(quad);
            quadArea.subtract(polyArea);
            boolean isInside = quadArea.isEmpty();
            if (isInside) {
                return area.area;
            }
        }

        
        


        return -1;
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
