public class RandomNumberGenerator {
    private static long a = 2493;
    private static long c = 1098;
    private static long M = 23123128321345L;
    private static long previous = 22;

    public static double NextRandom() {
        previous = ((a * previous) + c) % M;
        return (double) previous / M;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(NextRandom());
        }
    }
}