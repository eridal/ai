package eridal.ai.genetic;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public interface Problem<C extends Creature> {

    /**
     */
    public Comparator<C> comparator();

    /**
     * Pick the best creature, for the given Problem
     * by its fitness value
     */
    public C best(C x, C y);

    /**
     * Compares two creatures.
     * 
     * Returns a negative integer, zero, or a positive integer as the first argument 
     * is worst than, equal to, or better than the second.
     */
    public default int compare(C x, C y) {
        return comparator().compare(x, y);
    }

    /**
     * Returns the best {@link Creature} for this problem
     */
    public default C best(List<C> creatures) {
        return best(creatures.stream());
    }

    /**
     * Returns the best {@link Creature} for this problem
     */
    public default C best(Stream<C> creatures) {
        return creatures.min(comparator()).get();
    }

    /**
     * Returns the worst {@link Creature} for this problem
     */
    public default C worst(List<C> creatures) {
        return worst(creatures.stream());
    }

    /**
     * Returns the best {@link Creature} for this problem
     */
    public default C worst(Stream<C> creatures) {
        return creatures.max(comparator()).get();
    }
    
    public default <D extends Creature> Problem<D> cast() {
        @SuppressWarnings("unchecked")
        Problem<D> that = (Problem<D>) this;
        return that;
    }

    public default List<C> bestOf(List<C> elite, List<C> creatures) {

        if (elite.isEmpty()) {
            return creatures;
        }

        final int size = creatures.size();

        return Stream.concat(creatures.stream(), elite.stream())
                .sorted(comparator())
                .limit(size)
                .collect(Collectors.toList());
    }

    /**
     * Returns `true` if the {@link Creature} have a fitness
     * value that exceeds the given fitness target
     */
    public boolean done(double target, C creature);

    /**
     * Returns `true` if the {@link Creature Creatures} have an average fitness
     * value that exceeds the given fitness target
     */
    public boolean done(double target, List<C> creature);

    static <C extends Creature> Problem<C> MAXIMIZE() {
        return new Problem<C>() {

            @Override public C best(C x, C y) {
                return x.fitness() < y.fitness() ? y : x;
            }

            @Override public boolean done(double fitness, C creature) {
                return creature.fitness() > fitness;
            }

            @Override public boolean done(double fitness, List<C> creatures) {
                return Creature.average(creatures) > fitness;
            }

            final Comparator<C> DESC = Creature.descending();

            @Override public Comparator<C> comparator() {
                return DESC;
            }
        };
    }


    static <C extends Creature> Problem<C> MINIMIZE() { 
        return new Problem<C>() {

            @Override public C best(C x, C y) {
                return x.fitness() < y.fitness() ? x : y;
            }

            @Override public boolean done(double fitness, C creature) {
                return creature.fitness() < fitness;
            }

            @Override public boolean done(double fitness, List<C> creatures) {
                return Creature.average(creatures) < fitness;
            }

            final Comparator<C> ASC = Creature.ascending();

            @Override public Comparator<C> comparator() {
                return ASC;
            }
        };
    }
}
