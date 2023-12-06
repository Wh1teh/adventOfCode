package com.aoc.util;

public class ProgressBar {

    private int len;
    private int start;
    private int end;
    private int position;
    private int progressed = 0;

    public ProgressBar(int start, int end) {
        this.len = end - start - 2;
        this.start = start;
        this.end = end;
        this.position = start + 1;
    }

    public void initProgressBar() {
        XD.printAt("[", start);
        XD.printAt("]", end);
    }

    public void progress(double newProgress) {
        // System.out.println("new prog: " + newProgress + ", len: " + len +", len / new progs: " + (int) (len * newProgress));
        int catchUp = (int) (len * newProgress) + 1;

        // System.out.println("catch up: " + catchUp);
        for (; progressed < catchUp; progressed++) {
            XD.printAt(XD.style("#", XD.blue), position++);
        }
    }

    // getters and setters

    public int getStart() {
        return this.start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return this.end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getLen() {
        return this.len;
    }

    public void setLen(int len) {
        this.len = len;
    }

}
