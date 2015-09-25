package eridal.ai.utils;

public interface Filter {

    double apply(double x);

    public static final Filter NEGATIVE = lows(0.0, 1.0);
    public static final Filter POSITIVE = tops(0.0, 1.0);
    public static final Filter SPLIT    = tops(0.5, 1.0);

    public static Filter tops(double val, double k) {
        return x -> x > val ? k : 0.0;
    }

    public static Filter lows(double val, double k) {
        return x -> x < val ? k : 0.0;
    }
}
