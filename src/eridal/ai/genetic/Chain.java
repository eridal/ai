package eridal.ai.genetic;

import java.util.List;

public interface Chain<C extends Creature> {

    /**
     * 
     */
    public void tap(long loop, List<C> creatures, C best);
}
