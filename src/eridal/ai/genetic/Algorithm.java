package eridal.ai.genetic;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Algorithm<C extends Creature> {

    private Init<C> init;
    private Problem<C> problem;
    private Selector<C> selector;
    private Breeder<C> breeder;
    private Mutator<C> mutator;
    private Chain<C> chain;
    private Elite<C> elite;
    private Stop<C> stop;

    public Algorithm<C> init(final Init<C> init) {
        this.init = init;
        return this;
    }

    /**
     * @param size quantity of creatures to be born
     * @param factory factory method
     */
    public Algorithm<C> init(final int size, final Supplier<C> factory) {
        this.init = Init.FACTORY(size, factory);
        return this;
    }

    public Algorithm<C> init(final C[] creatures) {
        this.init = Init.FROM(creatures);
        return this;
    }

    public Algorithm<C> problem(final Problem<C> problem) {
        this.problem = problem;
        return this;
    }

    public Algorithm<C> select(final Selector<C> selector) {
        this.selector = selector;
        return this;
    }

    public Algorithm<C> breed(final Breeder<C> breeder) {
        this.breeder = breeder;
        return this;
    }

    /**
     * @param px probability to mutate
     */
    public Algorithm<C> mutate(final double px, final Mutator<C> mutator) {
        this.mutator = Mutator.RAND(px, mutator);
        return this;
    }

    public Algorithm<C> mutate(final Mutator<C> mutator) {
        this.mutator = mutator;
        return this;
    }

    public Algorithm<C> elite(final Elite<C> elite) {
        this.elite = elite;
        return this;
    }

    /**
     * Percentage of the population's best creatures that should survive
     * to the next round
     */
    public Algorithm<C> elite(final double ratio) { 
        this.elite = ratio > 0 ? Elite.BEST(ratio) : Elite.NONE();
        return this;
    }

    /**
     * Quantity of the best creatures that should survive
     * to the next round
     */
    public Algorithm<C> elite(final int size) {
        this.elite = size == 0 ? Elite.NONE() :
                     size == 1 ? Elite.BEST() : Elite.BEST(size) ;
        return this;
    }

    public Algorithm<C> stop(final Stop<C> stop) {
        this.stop = stop;
        return this;
    }

    /**
     * Stops executing when ANY of the given {@link Stop} criteria is met.
     */
    @SafeVarargs
    public final Algorithm<C> stop(final Stop<C> ...stops) {
        this.stop = Stop.ANY(stops);
        return this;
    }

    /**
     * Stops executing WHEN the given criteria is met
     */
    public Algorithm<C> stop(final Predicate<C> when) {
        this.stop = Stop.WHEN(when);
        return this;
    }

    /**
     * Stops when any {@link Creature} reaches the given fitness value.
     */
    public Algorithm<C> stop(final double targetFitness) {
        this.stop = Stop.FITNESS(targetFitness);
        return this;
    }

    /**
     * Stops executing when time is exhausted
     */
    public Algorithm<C> stop(long duration, TimeUnit unit) {
        this.stop = Stop.TIME(duration, unit);
        return this;
    }

    public Algorithm<C> maximize(final double fitness) {
        return problem(Problem.MAXIMIZE())
                .stop(fitness);
    }

    public Algorithm<C> minimize(final double fitness) {
        return problem(Problem.MINIMIZE())
                .stop(fitness);
    }

    public Algorithm<C> chain(Chain<C> chain) {
        this.chain = chain;
        return this;
    }

    public Result<C> execute() {

        Objects.requireNonNull(init, "Init required");
        Objects.requireNonNull(breeder, "Breeder required");
        Objects.requireNonNull(mutator, "Mutator requred");

        if (null == selector) {
            selector = Selector.RANKING();
        }

        if (null == problem) {
            problem = Problem.MAXIMIZE();
        }

        if (null == elite) {
            elite = Elite.NONE();
        }

        if (null == stop) {
            stop = Stop.TIME(30, TimeUnit.SECONDS);
        }

        final long start = System.currentTimeMillis();

        List<C> creatures = init.create();
        List<C> bests;

        for (long loop = 0; ; loop++) {

            if (null != chain) {
                chain.tap(
                    loop,
                    creatures,
                    problem.best(creatures)
                );
            }

            Stream<C> flow;

            flow = selector.select(creatures, problem);
            flow = breeder.breed(flow);
            flow = mutator.mutate(flow);

            bests = elite.save(creatures, problem);

            creatures = flow.collect(Collectors.toList());
            creatures = problem.bestOf(bests, creatures);

            if (stop.stop(creatures, problem)) {

                final long end = System.currentTimeMillis();

                return new Result<C>(
                    loop,
                    end - start,
                    creatures,
                    problem.best (creatures),
                    problem.worst(creatures)
                );
            }
        }
    }
}
