package eridal.ai.neural;

import java.util.ArrayList;
import java.util.List;

public class Neuron {

    /** output  */
    private double y;

    /** weights */
    private double w;

    /** error   */
    private double δ;

    private Squash fn;

    private List<Synapse> forward = new ArrayList<>();
    private List<Synapse> backward = new ArrayList<>();

    public Neuron(Squash fn) {
        this(fn, 1.0);
    }

    final int id;
    static int ID;

    public Neuron(Squash fn, double w) {
        this.id = ID++;
        this.fn = fn;
        this.w = w;
    }

    public void connect(Neuron n, double w) {
        Synapse s = new Synapse(this, n, w);
        forward.add(s);
        n.backward.add(s);
    }

    public double input(double x) {
        return y = fn.activate(w * x);
    }

    public double activate() {
        double x = 0;
        for (Synapse s : backward) {
            x += s.activate();
        }
        return input(x);
    }

    public double error(double η, double e) {

        δ = fn.error(y) * e;

        if (backward.isEmpty()) {
            w -= η * y * δ;
        }
        else {
            for (Synapse s : backward) {
                s.propagate(η);
            }
        }

        return δ;
    }

    public double propagate(double η) {

        double e = 0;

        for (Synapse s : forward) {
            e += s.error();
        }

        return error(η, e);
    }

    public double output() {
        return y;
    }

    public double error() {
        return δ;
    }
}
