package util;
import model.Interval;

public class RandomNumberGenerator {
    private static final long A = 2493;
    private static final long C = 1098;
    private static final long M = 23123128321345L;
    private static long previous = 22;

    public static double nextRandom() {
        previous = ((A * previous) + C) % M;
        return (double) previous / M;
    }

    public double randomInRange(Interval interval) {
        return interval.getLower() + ((interval.getUpper() - interval.getLower()) * nextRandom());
    }
}
