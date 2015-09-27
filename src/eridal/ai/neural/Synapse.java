package eridal.ai.neural;


public class Synapse {

    private final Neuron source;
    private final Neuron target;

    private double weight;

    public Synapse(Neuron source, Neuron target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    @Override public String toString() {
        final String s = source.id() == Neuron.BIAS ? "b" : String.format("n%d", source.id());
        final String t = target.id() == Neuron.BIAS ? "b" : String.format("n%d", target.id());
        return String.format("Synapse([%s]->[%s] w:%2.2f)", s, t, weight);
    }

    public Neuron source() {
        return source;
    }

    public Neuron target() {
        return target;
    }

    public void weight(double weight) {
        this.weight = weight;
    }

    public double weight() {
        return weight;
    }

    public double activate() {
        return weight * source.output();
    }

    public void propagate(double η) {
        weight -= η * source.output() * target.error();
    }

    public double error() {
        return weight * target.error();
    }

    public static double randomWeight() {
        return Math.random() * 2.0 - 1.0;
    }

}
