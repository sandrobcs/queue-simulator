package main.model;

public class Interval {
    private final double lower;
    private final double upper;

    public Interval(double lower, double upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public double getLower() {
        return lower;
    }

    public double getUpper() {
        return upper;
    }
}