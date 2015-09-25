package eridal.ai.neural;

public class Network {

    private Layer input;
    private Layer output;

    public Network(int ...layers) {

        Layer prev = null;

        for (int size : layers) {

            final Neuron[] neurons = new Neuron[size];
            final double w = 1.0 / size;

            while (size-- > 0) {

                final Neuron n = new Neuron(w);

                if (null != prev) {
                    for (Neuron p : prev.neurons) {
                        n.connect(p, w);
                    }
                }

                neurons[size] = n;
            }

            if (null == prev) {
                prev = input = new Layer(neurons);
            }
            else {
                prev = new Layer(neurons, prev);
            }
        }

        output = prev;
    }

    public Network(int[] layers, double[][] weigths) {

        Layer prev = null;

        for (int l = 0; l < layers.length; l++) {

            final Neuron[] neurons = new Neuron[layers[l]];

            for (int k = layers[l]; k-- > 0; ) {

                final Neuron n = new Neuron(weigths[l][k]);

                if (null != prev) {
                    for (Neuron p : prev.neurons) {
                        n.connect(p, weigths[l][k]);
                    }
                }

                neurons[k] = n;
            }

            if (null == prev) {
                prev = input = new Layer(neurons);
            }
            else {
                prev = new Layer(neurons, prev);
            }
        }

        output = prev;
    }

    public double[] input(double ...x) {
        return input.input(x);
    }
}
