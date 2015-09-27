package eridal.ai.utils;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class Nulls {

    public static <T> T coalesce(T val1, T val2) {
        return val1 != null ? val1 : Objects.requireNonNull(val2);
    }

    @SafeVarargs
    public static <T> T coalesce(T ...values) {
        for (T val : values) {
            if (null != val) {
                return val;
            }
        }
        throw new NullPointerException();
    }

    @SafeVarargs
    public static <T> T coalesce(Supplier<T> ... values) {
        for (Supplier<T> s : values) {
            T val = s.get();
            if (null != val) {
                return val;
            }
        }
        throw new NullPointerException();
    }
}
