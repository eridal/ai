package eridal.genetics;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eridal.genetics.utils.Rand;

public interface Breeder<C extends Creature> {

    /**
     * @param x
     * @param y
     * 
     * @return
     */
    public abstract C breed(final C x, final C y);

    /**
     * @param parents
     * @return
     */
    public default Stream<C> breed(final Stream<C> parents) {

        final List<C> creatures = parents.collect(Collectors.toList());

        return Stream.generate(() -> {
            final C x = Rand.element(creatures);
            final C y = Rand.element(creatures);
            return breed(x, y);
        }).limit(creatures.size());
    }
}
