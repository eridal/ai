package eridal.ai.neural;

import eridal.ai.math.Matrix;
import eridal.ai.utils.Filter;

public class Networks {

    /**
     * @param bias activation threshold
     * @param layers quantities of neurons to create
     */
    public static Network create(double bias, int...layers) {
        return new NetworkBuilder()
            .layers(layers)
            .bias(bias)
            .squash(Squashs.SIGMOID)
            .filter(Filter.HALF_STEP)
            .build();
    }

    /**
     * Creates a Perceptron network.
     *
     * @param bias activation threshold
     * @return the created Perceptron network
     */
    public static Network percepton(double bias) {
        double w0 = Synapse.randomWeight();
        double w1 = Synapse.randomWeight();
        return percepton(bias, w0, w1);
    }

    /**
     * Creates a Perceptron network.
     *
     * @param bias activation threshold
     * @param w0 weight for first input
     * @param w1 weight for second input
     *
     * @return the created Perceptron network
     */
    public static Network percepton(double bias, double w0, double w1) {

        final Neuron n0 = new Neuron(0);
        final Neuron n1 = new Neuron(1);
        final Neuron n2 = new Neuron(2);

        n0.synapseTo(n2, w0);
        n1.synapseTo(n2, w1);
        n2.bias(bias);

        Layer l0 = new Layer(n0, n1);
        Layer l1 = l0.createNext(n2);

        return new Network(l0,  l1, Filter.POSITIVE);
    }

    /**
     * Creates a Hopfield network
     *
     * @param patterns The patterns this network should recognize;
     *
     * @return The created network
     *
     * @throws IllegalArgumentException when all patterns not have the same length.
     */
    public static Network hopfield(double[] ...patterns) {
 
        if (patterns.length == 0 || patterns[0].length == 0) {
            throw new IllegalArgumentException("missing patterns");
        }

        int size = patterns[0].length;

        double[][] weights = new double[size][size];

        for (double[] p : patterns) {

            if (p.length != size) {
                throw new IllegalArgumentException("wrong pattern size");
            }

            double[][] m = Matrix.product(p, p);
            weights = Matrix.add(weights, m);
        }

        final Neuron[] i = new Neuron[size];
        final Neuron[] o = new Neuron[size];

        for (int k = size; k-- > 0; ) {
            i[k] = new Neuron(k);
            o[k] = new Neuron(k + size);
        }

        for (int s = o.length; s-- > 0;) {
            for (int t = o.length; t-- > 0;) {
                if (s == t) {
                    i[s].synapseTo(o[t], 1.0);
                }
                else {
                    o[s].synapseTo(o[t], weights[s][t]);
                }
            }
        }

        final Layer input = new Layer(i);
        final Layer output = input.createNext(o);

        return new Network(input, output, Math::signum);
    }
}
