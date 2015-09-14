package eridal.genetics;

import java.util.stream.Stream;

import eridal.genetics.utils.Rand;


public interface Mutator<C extends Creature> {

    /**
     * 
     */
    public C mutate(final C creature);

    /**
     * 
     */
    public default Stream<C> mutate(final Stream<C> population) {
        return population.map(this::mutate);
    }

    /**
     * 
     */
    public static <C extends Creature> Mutator<C> RAND(final double px, final Mutator<C> mutator) {
        return creature -> {
            if (Rand.real() < px) {
                return mutator.mutate(creature);
            }
            return creature;
        };
    }
}