package eridal.ai.utils;

public interface Filter {

    double apply(double x);

    public default double[] apply(double[] x) {
        double[] y = new double[x.length];
        for (int k = x.length; k-- > 0;) {
            y[k] = apply(x[k]);
        }
        return y;
    }

    public static final Filter NEGATIVE = lows(0.0, 1.0);
    public static final Filter POSITIVE = tops(0.0, 1.0);
    public static final Filter SPLIT    = tops(0.5, 1.0);

    public static Filter tops(double val, double k) {
        return x -> x > val ? k : 0.0;
    }

    public static Filter lows(double val, double k) {
        return x -> x < val ? k : 0.0;
    }

    public static final Filter NONE = new Filter() {
        @Override public double apply(double x) {
            return x;
        }
        @Override public double[] apply(double[] x) {
            return x;
        }
    };

    public static Filter optional(Filter filter) {
        return null != filter ? filter : NONE;
    }
}
