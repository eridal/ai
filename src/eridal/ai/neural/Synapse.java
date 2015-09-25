package eridal.ai.neural;


public class Synapse {

    private final Neuron source;
    private final Neuron target;

    private double w;

    public Synapse(Neuron source, Neuron target, double w) {
        this.source = source;
        this.target = target;
        this.w = w;
    }

    @Override public String toString() {
        return String.format("Synapse([n%d]->[n%d] w:%2.2f)", source.id(), target.id(), w);
    }

    public double activate() {
        return w * source.output();
    }

    public void propagate(double η) {
        w -= η * source.output() * target.error();
    }

    public double error() {
        return w * target.error();
    }
}
