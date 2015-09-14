package eridal.genetics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import eridal.genetics.utils.Rand;

public interface Selector<C extends Creature> {

    /**
     * 
     */
    public Stream<C> select(List<C> creatures, Problem<C> problem);


    public static <C extends Creature> Selector<C> RANKING() {
        return (creatures, problem) -> creatures.stream().sorted(problem.comparator());
    }

    /**
     * 
     */
    public static <C extends Creature> Selector<C> TOURNAMENT() {
        return (creatures, problem) -> Stream.generate(() -> {
            final C x = Rand.element(creatures);
            final C y = Rand.element(creatures);
            return problem.best(x, y);
        }).limit(creatures.size());
    }

    /**
     * 
     */
    public static <C extends Creature> Selector<C> ROULETE() {
        return (creatures, problem) -> {
            final double sum = creatures.stream()
                    .mapToDouble(Creature::fitness)
                    .sum();

            final double[] px = creatures.stream()
                    .mapToDouble(c -> c.fitness() / sum)
                    .toArray();

            for (int i = 1; i < px.length; i++) {
                px[i] += px[i - 1];
            }

            return roulete(creatures.size(), creatures, px);
        };
    }

    /**
     * 
     */
    public static <C extends Creature> Selector<C> CSNE() {
        return (creatures, problem) -> {

            final double average = Creature.average(creatures);

            if (average == 0) {
                return creatures.stream();
            }

            final double[] px = creatures.stream()
                    .mapToDouble(c -> c.fitness() / average)
                    .toArray();

            final List<C> winners = new ArrayList<>();

            double sum = 0.0;

            for (int i = 0; i < px.length; i++) {

                sum += px[i];
                int floor = (int) Math.floor(px[i]);

                px[i] = px[i] - floor;

                while (floor-- > 0) {
                    winners.add(creatures.get(i));
                }
            }

            if (sum == 0) {
                sum = 1.0;
                Arrays.fill(px, 1.0 / px.length);
            }

            px[0] = px[0] / sum;

            for (int i = 1; i < px.length; i++) {
                px[i] = px[i] / sum + px[i - 1]; 
            }

            return Stream.concat(
                winners.stream().limit(creatures.size()),
                roulete(creatures.size() - winners.size(), creatures, px)
            );
        };
    }

    public static <C extends Creature> Stream<C> roulete(final int size, final List<C> creatures, final double[] px) {

        if (size <= 0) {
            return Stream.empty();
        }

        return Stream.generate(() -> {
            final double r = Rand.real();
            for (int i = 0; i < px.length; i++) {
                if (px[i] > r) {
                    return creatures.get(i);
                }
            }
            return creatures.get(creatures.size() - 1);
        }).limit(size);
    }
}
