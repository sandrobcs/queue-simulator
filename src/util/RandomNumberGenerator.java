package util;
import model.Interval;

public class RandomNumberGenerator {
    private final long A = 2493;
    private final long C = 1098;
    private final long M = 23123128321345L;
    private long previous = 22;
    private int count;

    public double nextRandom() {
        previous = ((A * previous) + C) % M;
        count++;
        return (double) previous / M;
    }

    public double randomInRange(Interval interval) {
        return interval.getLower() + ((interval.getUpper() - interval.getLower()) * nextRandom());
    }

    public int getCount() {
        return count;
    }
}
