package eridal.ai.neural;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Neuron implements Iterable<Synapse> {

    private final int id;

    /** output */
    private double y;

    /** error */
    private double δ;

    /** squash function */
    private Squash squash;

    private List<Synapse> targets = new ArrayList<>();
    private List<Synapse> sources = new ArrayList<>();

    public Neuron(int id, Squash squash) {
        this.id = id;
        this.squash = squash;
    }

    public int id() {
        return id;
    }

    @Override public String toString() {
        final String synap;
        if (sources.isEmpty()) {
            // input neuron
            synap = String.format("%d->n", targets.size());
        }
        else if (targets.isEmpty()) {
            // output neuron
            synap = String.format("n->%d", sources.size());
        }
        else {
            // hidden neuron
            synap = String.format("%d->n->%d", sources.size(), targets.size());
        }
        return String.format("Neuron(id=%d y=%2.2f δ=%2.2f synap=[%s])", id, y, δ, synap);
    }

    public void connect(Neuron n, double w) {
        Synapse s = new Synapse(this, n, w);
        targets.add(s);
        n.sources.add(s);
    }

    public double input(double x) {
        return y = x;
    }

    public double activate() {
        double x = 0;
        for (Synapse s : sources) {
            x += s.activate();
        }
        return y = squash.activate(x);
    }

    public double error(double η, double e) {

        δ = squash.error(y) * e;

        for (Synapse s : sources) {
            s.propagate(η);
        }

        return δ;
    }

    public double propagate(double η) {

        double e = 0;

        for (Synapse s : targets) {
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

    @Override public Iterator<Synapse> iterator() {
        return forwardSynapses();
    }

    public Iterator<Synapse> forwardSynapses() {
        return targets.iterator();
    }

    public Iterator<Synapse> backwardSynapses() {
        return sources.iterator();
    }
}
