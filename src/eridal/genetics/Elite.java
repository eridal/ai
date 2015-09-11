package eridal.genetics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Elite<C extends Creature> {

    public List<C> save(List<C> creatures, Problem<C> problem);

    public default List<C> collect(Stream<C> creatures) {
        return creatures.collect(Collectors.toList());
    }

    public static <C extends Creature> Elite<C> NONE() {
        return (creatures, problem) -> Collections.emptyList();
    }

    public static <C extends Creature> Elite<C> BEST() {
        return (creatures, problem) -> Arrays.asList(problem.best(creatures));
    }

    public static <C extends Creature> Elite<C> BEST(final int size) {
        return (creatures, problem) -> creatures.stream()
                .sorted(problem.comparator())
                .limit(size)
                .collect(Collectors.toList())
                ;
    }

    public static <C extends Creature> Elite<C> BEST(final double ratio) {
        return (creatures, problem) -> creatures.stream()
            .sorted(problem.comparator())
            .limit((int) Math.max(1, creatures.size() * ratio))
            .collect(Collectors.toList())
            ;
    }
}
