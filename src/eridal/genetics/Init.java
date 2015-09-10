package eridal.genetics;

import java.util.List;
import java.util.function.Supplier;

public interface Init<C extends Creature> {

    public List<C> create();

    public static <C extends Creature> Init<C> FACTORY(final int size, final Supplier<C> factory) {
        return () -> Creature.collect(
            Creature.factory(size, factory)
        );
    }
}