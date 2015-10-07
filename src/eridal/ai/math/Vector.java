package eridal.ai.math;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

public abstract class Vector {

    private Vector() {
        throw new UnsupportedOperationException();
    }

    public static double[] apply(double[] v, DoubleUnaryOperator op) {

        double[] result = new double[v.length];

        for (int k = v.length; k-- > 0; ) {
            result[k] = op.applyAsDouble(result[k]);
        }

        return result;
    }

    public static double[] apply(double[] v1, double[] v2, DoubleBinaryOperator op) {

        assertSameLength(v1, v2);

        double[] result = new double[v1.length];

        for (int k = v1.length; k-- > 0; ) {
            result[k] = op.applyAsDouble(v1[k], v2[k]);
        }

        return result;
    }

    public static double[] add(double[] v1, double[] v2) {
        return apply(v1, v2, (x, y) -> x + y);
    }

    public static double[] substract(double[] v1, double[] v2) {
        return apply(v1, v2, (x, y) -> x - y);
    }

    public static double[] product(double[] v1, double[] v2) {
        return apply(v1, v2, (x, y) -> x * y);
    }

    public static double dot(double[] v1, double[] v2) {
        return sum(product(v1, v2));
    }

    private static void assertSameLength(double[] v1, double[] v2) {
        if (v1.length != v2.length) {
            throw new IllegalArgumentException();
        }
    }

    public static double reduce(double[] values, DoubleBinaryOperator op) {
        return reduce(values, op, 0);
    }

    public static double reduce(double[] values, DoubleBinaryOperator op, double initial) {
        double result = initial;
        for (double value : values) {
            result = op.applyAsDouble(result, value);
        }
        return result;
    }

    public static double sum(double[] values) {
        return reduce(values, (x, y) -> x + y);
    }

    public static double average(double[] values) {
        return values.length > 0 ? sum(values) / values.length : 0;
    }
}
