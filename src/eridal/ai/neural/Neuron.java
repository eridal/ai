package eridal.ai.neural;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eridal.ai.utils.Nulls;

public class Neuron {

    private final int id;
    private final Squash squash;

    private Synapse bias;

    /** output */
    private double y;

    /** error */
    private double δ;


    final List<Synapse> sources = new ArrayList<>();
    final List<Synapse> targets = new ArrayList<>();

    public Neuron(int id, Squash squash) {
        this.id = id;
        this.squash = Nulls.coalesce(squash, Squashs.IDENTITY);
    }

    public Neuron(int id) {
        this(id, null);
    }

    public int id() {
        return id;
    }

    /**
     * Connects two neurons.
     *
     * <pre>
     *            weight
     * (source) ---------> (target)
     * </pre>
     *
     * @param target Neuron that will react when by this Neuron activates
     * @param weight How much the stimuli will affect the target neuron
     *
     * @return The {@link Synapse} created between the neurons
     */
    public Synapse synapseTo(Neuron target, double w) {

        final Synapse s = new Synapse(this, target, w);

        this.targets.add(s);
        target.sources.add(s);

        return s;
    }

    /**
     * Connects two neurons.
     *
     * <pre>
     * (source) ---------> (target)
     * </pre>
     *
     * @param target Neuron that will react when by this Neuron activates
     *
     * @return The {@link Synapse} created between the neurons
     */
    public Synapse synapseTo(Neuron target) {
        double w = Synapse.randomWeight();
        return synapseTo(target, w);
    }

    @Override public String toString() {
        final String n = id == BIAS ? "bias" : String.format("id:%d", id);
        final String in  = sources.isEmpty() ? "" : String.format("%d->", sources.size());
        final String out = targets.isEmpty() ? "" : String.format("->%d", targets.size());
        return String.format("Neuron(%s y:%2.2f δ:%2.2f synap:[%sn%s])", n, y, δ, in, out);
    }

    static final int BIAS = -0xB1A5;

    /**
     * @return The hidden bias connection of this Neuron
     */
    public Neuron bias() {
        return null == bias ? null : bias.source();
    }

    /**
     * Adds a hidden bias Neuron, as source to this Neuron;
     * or changes the bias value if it already exists.
     *
     * @param Θ the initial bias value
     * @return The bias Neuron
     */
    public Neuron bias(double Θ) {

        // do we already have bias?
        if (null != bias) {
            bias.weight(Θ);
            return bias.source();
        }

        // well.. let's add it 
        final Neuron n = new Neuron(BIAS);
        n.input(1.0);
        bias = n.synapseTo(this, Θ);
        return n;
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

    public Iterable<Synapse> sources() {
        return Collections.unmodifiableList(sources);
    }

    public Iterable<Synapse> targets() {
        return Collections.unmodifiableList(targets);
    }
}
