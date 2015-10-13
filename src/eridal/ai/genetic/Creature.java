package eridal.ai.genetic;

import java.util.Comparator;
import java.util.List;

public interface Creature {

    public abstract double fitness();

    public static <C extends Creature> double average(List<C> creatures) {
        return creatures.stream()
                .mapToDouble(Creature::fitness)
                .average()
                .orElse(0.0d);
    }

    public static <C extends Creature> Comparator<C> ascending() {
        return Comparator.comparing(Creature::fitness);
    }

    public static <C extends Creature> Comparator<C> descending() {
        return Creature.<C>ascending().reversed();
    }
}
