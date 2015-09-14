package eridal.genetics;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

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

        final AtomicReference<C> prev = new AtomicReference<>();

        return parents.map(y -> {
            final C x = prev.getAndSet(y);
            return null == x ? y : breed(x, y);
        });
    }
}
