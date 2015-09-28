package eridal.ai.utils;

import java.util.function.DoubleBinaryOperator;

public class ArrayMath {

    public static double sum(final double[] values) {
        double sum = 0;
        for (double v : values) {
            sum += v;
        }
        return sum;
    }

    public static double[][] product(final double[] v1, double[] v2) {

        double[][] m = new double[v1.length][v2.length];

        for (int i = 0; i < v1.length; i++) {
            for (int j = 0; j < v2.length; j++) {
                m[i][j] = v1[i] * v2[j];
            }
        }

        return m;
    }

    public static double[] sum(double[] v1, double[] v2) {
        return apply(v1, v2, (x, y) -> x + y);
    }

    public static double[] diff(double[] v1, double[] v2) {
        return apply(v1, v2, (x, y) -> x - y);
    }

    public static double[] dot(double[] v1, double[] v2) {
        return apply(v1, v2, (x, y) -> x * y);
    }

    public static double[] apply(double[] v1, double[] v2, DoubleBinaryOperator op) {
        double[] r = new double[v1.length];
        for (int k = v1.length; k-- > 0; ) {
            r[k] = op.applyAsDouble(v1[k], v2[k]);
        }
        return r;
    }

    public static double[][] sum(final double[][] m1, double[][] m2) {
        return apply(m1, m2, (x, y) -> x + y);
    }

    public static double[][] xor(final double[][] m1, double[][] m2) {
        return apply(m1, m2, (x, y) -> {
            long xl = Double.doubleToRawLongBits(x);
            long yl = Double.doubleToRawLongBits(y);
            return Double.longBitsToDouble(xl ^ yl);
        });
    }

    public static double[][] apply(final double[][] m1, double[][] m2, DoubleBinaryOperator op) {

        double[][] m = new double[m1.length][m1[0].length];

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[i].length; j++) {
                m[i][j] = op.applyAsDouble(m1[i][j], m2[i][j]);
            }
        }

        return m;
    }

    public static int sum(final int[] values) {
        int sum = 0;
        for (int v : values) {
            sum += v;
        }
        return sum;
    }

}
