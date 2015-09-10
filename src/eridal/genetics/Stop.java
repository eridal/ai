package eridal.genetics;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface Stop<C extends Creature> {

    /**
     * Returns {@literal true} if the algorithm has more loops.
     */
    boolean stop(List<C> creatures, Problem<C> problem);

    /**
     * Stops after {@code count} loops.
     */
    public static <C extends Creature> Stop<C> LOOPS(final long count) {
        return new Stop<C>() {
            long i = count;
            @Override public boolean stop(List<C> creatures, Problem<C> problem) {
                return --i <= 0;
            }
        };
    }

    /**
     * Stops when any {@link Creature} reaches the given fitness value.
     */
    public static <C extends Creature> Stop<C> FITNESS(final double fitnessTarget) {
        return (creatures, problem) -> {
            return problem.done(fitnessTarget, creatures);
        };
    }

    /**
     * Stops when the {@link Creature}s reaches the given average fitness value.
     */
    public static <C extends Creature> Stop<C> AVERAGE(final double fitnessTarget) {
        return (creatures, problem) -> {
            return problem.done(fitnessTarget, creatures);
        };
    }

    /**
     * Stops executing when time is exhausted
     */
    public static <C extends Creature> Stop<C> TIME(long duration, TimeUnit unit) {
        final long until = System.currentTimeMillis() + unit.toMillis(duration);
        return (creatures, problem) -> {
            return until <= System.currentTimeMillis();
        };
    }

    /**
     * Stops executing when any of the given {@link Stop}s stops.
     */
    @SafeVarargs
    public static <C extends Creature> Stop<C> ANY(final Stop<C> ...stops) {
        if (stops.length == 0) {
            throw new IllegalArgumentException("empty stoppers array");
        }
        if (stops.length == 1) {
            return stops[0];
        }

        return (creatures, problem) -> Arrays.stream(stops)
                .anyMatch(s -> s.stop(creatures, problem))
        ;
    }

    /**
     * Stops executing when all of the given {@link Stop}s stops.
     */
    @SafeVarargs
    public static <C extends Creature> Stop<C>  ALL(final Stop<C> ...stops) {
        if (stops.length == 0) {
            throw new IllegalArgumentException("empty stoppers array");
        }
        if (stops.length == 1) {
            return stops[0];
        }

        return (creatures, problem) -> Arrays.stream(stops)
                .allMatch(s -> s.stop(creatures, problem))
        ;
    }
}
