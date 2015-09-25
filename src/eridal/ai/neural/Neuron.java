package eridal.ai.neural;

import java.util.ArrayList;
import java.util.List;

public class Neuron {

    /** output  */
    private double y;

    /** error   */
    private double δ;

    private Squash fn;

    private List<Synapse> forward = new ArrayList<>();
    private List<Synapse> backward = new ArrayList<>();

    final int id;
    static int ID;

    public Neuron(Squash fn) {
        this.id = ID++;
        this.fn = fn;
    }

    public void connect(Neuron n, double w) {
        Synapse s = new Synapse(this, n, w);
        forward.add(s);
        n.backward.add(s);
    }

    public double input(double x) {
        return y = x;
    }

    public double activate() {
        double x = 0;
        for (Synapse s : backward) {
            x += s.activate();
        }
        return input(fn.activate(x));
    }

    public double error(double η, double e) {

        δ = fn.error(y) * e;

        for (Synapse s : backward) {
            s.propagate(η);
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
