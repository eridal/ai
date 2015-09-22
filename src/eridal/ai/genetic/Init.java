package eridal.ai.genetic;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Init<C extends Creature> {

    public List<C> create();

    public static <C extends Creature> Init<C> FACTORY(final int size, final Supplier<C> factory) {
        return () -> Stream.generate(factory)
                .limit(size)
                .collect(Collectors.toList())
                ;
    }
}