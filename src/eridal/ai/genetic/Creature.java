package eridal.ai.genetic;

import java.util.List;

public interface Creature {

    public abstract double fitness();

    public static <C extends Creature> double average(List<C> creatures) {
        return creatures.stream()
                .mapToDouble(Creature::fitness)
                .average()
                .orElse(0.0d);
    }
}
