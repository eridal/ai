package eridal.ai.neural;


public class Synapse {

    private final Neuron in;
    private final Neuron out;

    private double w;

    public Synapse(Neuron in, Neuron out, double w) {
        this.in = in;
        this.out = out;
        this.w = w;
    }

    public double activate(double x) {
        return w * x;
    }

    public double activate() {
        return w * in.output();
    }

}
