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

    /**
     * 
     */
    public static <C extends Creature> Selector<C> TOURNAMENT() {
        return (creatures, problem) -> Creature.factory(creatures.size(), () -> {
            final int rx = Rand.integer(creatures.size());
            final int ry = Rand.integer(creatures.size());
            final C x = creatures.get(rx);
            final C y = creatures.get(ry);
            return problem.best(x, y);
        });
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

            return Helper.roulete(creatures, px);
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

            return Stream.concat(winners.stream(), Helper.roulete(creatures, px));
        };
    }

    static class Helper {
        public static <C extends Creature> Stream<C> roulete(final List<C> creatures, final double[] px) {
            return Creature.factory(creatures.size(), () -> {
                final double r = Rand.real();
                for (int i = 0; i < px.length; i++) {
                    if (px[i] > r) {
                        return creatures.get(i);
                    }
                }
                return creatures.get(creatures.size() - 1);
            });
        }
    }
}
