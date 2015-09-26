package eridal.ai.neural;

import eridal.ai.utils.ArrayMath;
import eridal.ai.utils.Filter;

public class Networks {

    /**
     * Returns a Perceptron network.
     *
     * @param bias activation bias
     * @return the created Perceptron
     */
    public static Network percepton(double bias) {
        double w0 = Synapse.randomWeight();
        double w1 = Synapse.randomWeight();
        return percepton(bias, w0, w1);
    }


    public static Network percepton(double bias, double w0, double w1) {

        Neuron n0 = new Neuron(0, bias, Squashs.IDENTITY);
        Neuron n1 = new Neuron(1, bias, Squashs.IDENTITY);
        Neuron n2 = new Neuron(2, bias, Squashs.IDENTITY);

        Synapse.plug(n0, w0, n2);
        Synapse.plug(n1, w1, n2);

        Layer l0 = new Layer(n0, n1);
        Layer l1 = l0.createNext(n2);

        return new Network(l0,  l1, Filter.POSITIVE);
    }

    /**
     * @param bias activation bias
     * @param layers 
     * @return
     */
    public static Network backPropagation(double bias, int...layers) {
        return new NetworkBuilder()
            .layers(layers)
            .bias(bias)
            .squash(Squashs.SIGMOID)
            .filter(Filter.SPLIT)
            .build();
    }

    public static Network hopfield(int size, double[] v0, double[] v1) {

        if (size < 1 || v0.length != size || v1.length != size) {
            throw new IllegalArgumentException();
        }

        final Neuron[] i = new Neuron[size];
        final Neuron[] o = new Neuron[size];

        for (int k = size; k-- > 0; ) {
            i[k] = new Neuron(k);
            o[k] = new Neuron(k + size);
        }

        final double[][] m0 = ArrayMath.product(v0, v0);
        final double[][] m1 = ArrayMath.product(v1, v1);
        final double[][] m  = ArrayMath.sum(m0, m1);

        for (int s = o.length; s-- > 0;) {
            for (int t = o.length; t-- > 0;) {
                if (s == t) {
                    Synapse.plug(i[s], 1.0, o[t]);
                }
                else {
                    Synapse.plug(o[s], m[s][t], o[t]);
                }
            }
        }

        final Layer input = new Layer(i);
        final Layer output = input.createNext(o);

        return new Network(input, output, Math::signum);
    }

}
