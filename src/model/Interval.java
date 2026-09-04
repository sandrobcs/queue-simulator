package model;
public class Interval {
    private final int lower;
    private final int upper;

    public Interval(int lower, int upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public int getLower() {
        return lower;
    }

    public int getUpper() {
        return upper;
    }
}