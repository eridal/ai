package eridal.ai.neural;

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

}
