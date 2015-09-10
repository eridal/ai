package eridal.genetics;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Creature {

    public abstract double fitness();

    public static <C extends Creature> double average(List<C> creatures) {
        return creatures.stream()
                .mapToDouble(Creature::fitness)
                .average()
                .orElse(0.0d);
    }

    static <C extends Creature> Stream<C> factory(final int size, final Supplier<C> supplier) {
        return Stream.generate(supplier)
            .parallel()
            .limit(size)
        ;
    }

    public static <C extends Creature> List<C> collect(Stream<C> stream) {
        return stream.collect(Collectors.toList());
    }
}
