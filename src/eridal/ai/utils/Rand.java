package eridal.ai.utils;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Random;

public abstract class Rand {

    private Rand() {
        throw new UnsupportedOperationException();
    }

    private static final Random RANDOM = new Random();

    /**
     * Returns the next pseudo-random, uniformly distributed double 
     * value between 0.0d (inclusive) to 1.0d (exclusive)
     * 
     * @return a double from the range {@code [0.0d-1.0d)}
     */
    public static double real() {
        return RANDOM.nextDouble();
    }

    /**
     * Returns a pseudo-random, uniformly distributed double 
     * value between {@code min} (inclusive) to {@code max} (exclusive)
     * 
     * @return a double from the range {@code [min-max)}
     * 
     * @throws IllegalArgumentException if {@code max <= min}
     */
    public static double real(double minInclusive, double maxExclusive) {
        if (maxExclusive <= minInclusive) {
            throw new IllegalArgumentException();
        }
        return RANDOM.nextDouble() * (maxExclusive - minInclusive) + minInclusive;
    }

    /**
     * Returns a pseudo-random, uniformly distributed int value 
     * between 0 (inclusive) and the specified value (exclusive)
     * 
     * @return an int from the range {@code [0-max)}
     * 
     * @throws IllegalArgumentException if {@code max} is not positive
     */
    public static int integer(int maxExclusive) {
        return RANDOM.nextInt(maxExclusive);
    }

    /**
     * Returns a pseudo-random, uniformly distributed int value 
     * between {@code min} (inclusive) and {@code max} (exclusive)
     * 
     * @return an int from the range {@code [min-max)}
     * 
     * @throws IllegalArgumentException if {@code max} is not positive
     */
    public static int integer(int minInclusive, int maxExclusive) {
        return integer(maxExclusive - minInclusive) + minInclusive;
    }

    /**
     * Returns a pseudo-randomly chosen element from the given array
     */
    public static <T> T element(T[] values) {
        final int index = integer(values.length);
        return values[index];
    }

    /**
     * Returns a pseudo-randomly chosen element from the given List
     */
    public static <T> T element(List<T> values) {
        final int index = integer(values.size());
        return values.get(index);
    }

    /**
     * Returns a pseudo-randomly chosen list of elements from the given array.
     * 
     * Note that the list may contain duplicates, as they are randomly picked
     * 
     * @return an array of size {@size} with the chosen elements
     * @throws IllegalArgumentException if {@code size} is not positive
     */
    public static <T> T[] take(final T[] values, int size) {

        if (size < 0) {
            throw new IllegalArgumentException();
        }

        @SuppressWarnings("unchecked")
        final T[] result = (T[]) Array.newInstance(values.getClass().getComponentType(), size);

        while (size-- > 0) {
            result[size] = element(values);
        }

        return result;
    }

    /**
     * Returns a pseudo-randomly chosen element from the given enum class
     */
    public static <E extends Enum<E>> E element(Class<E> enumClass) {
        final E[] values = enumClass.getEnumConstants();
        return element(values);
    }

    /**
     * Returns a pseudo-randomly chosen list of elements from the given enum class.
     * 
     * Note that the list may contain duplicates, as they are randomly picked
     * 
     * @return an array of size {@size} with the chosen elements
     * @throws IllegalArgumentException if {@code size} is not positive
     */
    public static <E extends Enum<E>> E[] take(Class<E> enumClass, int size) {
        return take(enumClass.getEnumConstants(), size);
    }

}
