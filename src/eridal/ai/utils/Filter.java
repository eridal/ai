package eridal.ai.utils;


public interface Filter {

    /**
     * @param x
     * @return y = f(x)
     */
    double apply(double x);

    /**
     * Apply the filter to all values
     */
    public default double[] apply(double[] X) {
        double[] Y = new double[X.length];
        for (int k = X.length; k-- > 0;) {
            Y[k] = apply(X[k]);
        }
        return Y;
    }

    /**
     * f(x) = | 1  x < 0
     *        | 0  other
     */
    public static final Filter NEGATIVE = LOW_PASS(0, 1.0);

    /**
     * f(x) = | 1  x > 0
     *        | 0  other
     */
    public static final Filter POSITIVE = HIGH_PASS(0, 1.0);

    /**
     * f(x) = | 1  x > 0.5
     *        | 0  other
     */
    public static final Filter HALF_STEP = HIGH_PASS(0.5, 1.0);

    /**
     * f(x) = | k   x > min
     *        | 0   other
     */
    public static Filter HIGH_PASS(double min, double k) {
        return x -> x > min ? k : 0;
    }

    /**
     * f(x) = | k   min < x < max
     *        | 0   other
     */
    public static Filter BAND_PASS(double min, double max, double k) {
        return x -> min <= x && x <= max ? k : 0;
    }

    /**
     * f(x) = | 0   min < x < max
     *        | k   other
     */
    public static Filter BAND_STOP(double min, double max, double k) {
        return x -> min < x && x < max ? 0 : k;
    }

    /**
     * f(x) = | k   x < max
     *        | 0   other
     */
    public static Filter LOW_PASS(double max, double k) {
        return x -> x < max ? k : 0;
    }

    /**
     * f(x) = x ; ∀x
     */
    public static final Filter NONE = new Filter() {

        @Override public double apply(double x) {
            return x;
        }

        @Override public double[] apply(double[] x) {
            return x;
        }

    };
}
