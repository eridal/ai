package eridal.genetics;

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
     * Returns the best {@link Creature} for this problem
     */
    public default C best(List<C> creatures) {
        return creatures.stream().min(comparator()).get();
    }

    /**
     * Returns the worst {@link Creature} for this problem
     */
    public default C worst(List<C> creatures) {
        return creatures.stream().max(comparator()).get();
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

            @Override public Comparator<C> comparator() {
                return Comparator.<C, Double>comparing(Creature::fitness).reversed();
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

            @Override public Comparator<C> comparator() {
                return Comparator.comparing(Creature::fitness);
            }
        };
    }
}
