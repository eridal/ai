package eridal.ai.utils;

public class ArrayMath {

    public static double sum(final double[] values) {
        double sum = 0;
        for (double v : values) {
            sum += v;
        }
        return sum;
    }

    public static int sum(final int[] values) {
        int sum = 0;
        for (int v : values) {
            sum += v;
        }
        return sum;
    }

}
