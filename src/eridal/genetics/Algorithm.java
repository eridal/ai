package eridal.genetics;

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

    public Algorithm<C> init(final int size, final Supplier<C> factory) {
        return init(Init.FACTORY(size, factory));
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

    public Algorithm<C> mutate(final double px, final Mutator<C> mutator) {
        return mutate(Mutator.RAND(px, mutator));
    }

    public Algorithm<C> mutate(final Mutator<C> mutator) {
        this.mutator = mutator;
        return this;
    }

    public Algorithm<C> elite(Elite<C> elite) {
        this.elite = elite;
        return this;
    }

    @SafeVarargs
    public final Algorithm<C> stop(final Stop<C> ...stops) {
        this.stop = Stop.ANY(stops);
        return this;
    }

    public Algorithm<C> stop(Predicate<C> when) {
        return stop(Stop.WHEN(when));
    }

    public Algorithm<C> stop(final double targetFitness) {
        return stop(Stop.FITNESS(targetFitness));
    }

    public Algorithm<C> maximize(final double fitness) {
        return problem(Problem.MAXIMIZE()).stop(fitness);
    }

    public Algorithm<C> minimize(final double fitness) {
        return problem(Problem.MINIMIZE()).stop(fitness);
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
            selector = Selector.TOURNAMENT();
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
                    problem.best(creatures),
                    problem.worst(creatures)
                );
            }

            if (null != chain) {
                final C best = problem.best(bests.isEmpty() ? creatures : bests);
                final C worst = problem.worst(creatures);
                chain.tap(loop, creatures, best, worst);
            }
        }
    }
}
