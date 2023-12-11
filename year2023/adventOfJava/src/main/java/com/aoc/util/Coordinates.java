package com.aoc.util;

public class Coordinates implements Comparable<Coordinates> {
    public int yStart;
    public int xStart;
    public int yEnd;
    public int xEnd;

    public Coordinates(int yStart, int xStart) {
        this.yStart = yStart;
        this.yEnd = yStart;
        this.xStart = xStart;
        this.xEnd = xStart;
    }

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

    public void setCoordinates(Coordinates newCoordinates) {
        this.yStart = newCoordinates.getYs();
        this.yEnd = newCoordinates.getYe();
        this.xStart = newCoordinates.getXs();
        this.xEnd = newCoordinates.getXe();
    }

    public void decXs() {
        this.xStart--;
    }

    public void incXs() {
        this.xStart++;
    }

    public void decYs() {
        this.yStart--;
    }

    public void incYs() {
        this.yStart++;
    }
    // ------

    public int getYs() {
        return yStart;
    }

    public int getXs() {
        return xStart;
    }

    public int getYe() {
        return yEnd;
    }

    public int getXe() {
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

    @Override
    public int compareTo(Coordinates that) {
        int result = Integer.compare(this.yStart, that.yStart);
        if (result != 0) {
            return result;
        }

        result = Integer.compare(this.xStart, that.xStart);
        if (result != 0) {
            return result;
        }

        result = Integer.compare(this.yEnd, that.yEnd);
        if (result != 0) {
            return result;
        }

        return Integer.compare(this.xEnd, that.xEnd);
    }

}