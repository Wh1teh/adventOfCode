package aoc.aoc.util;

public record Coordinate(int y, int x) implements Comparable<Coordinate> {

    @Override
    public int compareTo(Coordinate o) {
        int compareY = Integer.compare(this.y, o.y);
        return compareY != 0 ? compareY : Integer.compare(this.x, o.x);
    }

    public int distanceFromPosition(Coordinate to) {
        return Math.abs(this.y() - to.y()) + Math.abs(this.x() - to.x());
    }
}
