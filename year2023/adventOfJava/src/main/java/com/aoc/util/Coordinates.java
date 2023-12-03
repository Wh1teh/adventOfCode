package com.aoc.util;

public class Coordinates {
    public final int yStart;
    public final int xStart;
    public final int yEnd;
    public final int xEnd;

    public Coordinates(int yStart, int yEnd, int xStart, int xEnd) {
        this.yStart = yStart;
        this.xStart = xStart;
        this.yEnd = yEnd;
        this.xEnd = xEnd;
    }

    public boolean partiallyOverlaps(Coordinates that) {
        // check horizontal
        if (xEnd < that.xStart || that.xEnd < xStart) {
            return false;
        }

        // check vertical
        if (yEnd < that.yStart || that.yEnd < yStart) {
            return false;
        }

        return true;
    }

    // ------

    public int getYStart() {
        return yStart;
    }

    public int getXStart() {
        return xStart;
    }

    public int getYEnd() {
        return yEnd;
    }

    public int getXEnd() {
        return xEnd;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Coordinates that = (Coordinates) obj;

        return yStart == that.yStart &&
                xStart == that.xStart &&
                yEnd == that.yEnd &&
                xEnd == that.xEnd;
    }

    @Override
    public int hashCode() {
        int result = yStart;
        result = 31 * result + xStart;
        result = 31 * result + yEnd;
        result = 31 * result + xEnd;
        return result;
    }

    @Override
    public String toString() {
        return "{ys: %d, ye: %d, ".formatted(yStart, yEnd) +
                "xs: %d, xe: %d}".formatted(xStart, xEnd);
    }
}