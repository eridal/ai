package eridal.ai.neural;

import java.util.ArrayList;
import java.util.List;

public class Neuron {

    private double y;
    private double w;

    /*
    private double δ;
    private double ε;
    */

    private List<Synapse> synapses = new ArrayList<>();

    public Neuron() {
        this(1.0);
    }

    public Neuron(double w) {
        this.w = w;
    }

    public void connect(Neuron in, double w) {
        synapses.add(new Synapse(in, this, w));
    }

    public double input(double x) {
        return  y = w * x;
    }

    public double activate() {
        return y = synapses.stream()
                .mapToDouble(Synapse::activate)
                .sum();
    }

    public double output() {
        return y;
    }
}
