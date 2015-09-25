package eridal.ai.neural;

public class Thresholds {

    static final Threshold SPLIT = x -> x > 0.5 ? 1.0 : 0.0;
}
