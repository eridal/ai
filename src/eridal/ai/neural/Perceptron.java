package eridal.ai.neural;

import eridal.ai.utils.Filter;

public class Perceptron {

    public final double η;
    private final Network net;

    public Perceptron(double w0,
                      double w1,
                      double Θ,
                      double η) {

        Neuron n0 = new Neuron(0, Θ);
        Neuron n1 = new Neuron(1, Θ);
        Neuron n2 = new Neuron(2, Θ);

        Synapse.plug(n0, w0, n2);
        Synapse.plug(n1, w1, n2);

        Layer l0 = new Layer(n0, n1);
        Layer l1 = l0.createNext(n2);

        this.net = new Network(l0,  l1, Filter.POSITIVE);
        this.η = η;
    }

    public Perceptron(double Θ,
                      double η) {
        this(0.5, 0.5, Θ, η);
    }

    public Perceptron() {
        this(0.5, 0.5, 0.3, 0.1);
    }

    /**
     * Trains the model
     * 
     * @return a boolean indicating if the model have just learn something new 
     */
    public boolean train(int x, int y, int result) {

        double error = classify(x, y) - Integer.signum(result);

        if (error != 0) {
            net.propagate(η, error);
        }

        return error != 0;
    }

    public int classify(int x0, int x1) {
        double[] y = net.execute(new double[] {x0, x1});
        return (int) y[0];
    }

}
