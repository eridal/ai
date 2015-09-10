package eridal.genetics;

import java.util.List;

public class Result<C extends Creature> {

    public final List<C> creatures;
    public final C best;
    public final C worst;

    public final double average;

    public final long loops;
    public final long duration;

    Result(long loops, long duration,
           List<C> creatures, C best, C worst) {

        this.loops = loops;
        this.duration = duration;

        this.creatures = creatures;
        this.best = best;
        this.worst = worst;

        average = Creature.average(creatures);
    }
}
