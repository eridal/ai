package eridal.ai.neural;


public class Network {

    private Layer input;
    private Layer output;

    public Network(Squash fn, int ...layers) {

        Layer prev = null;

        for (int size : layers) {

            final Neuron[] neurons = new Neuron[size];
            final double w = 1.0 / size;

            while (size-- > 0) {

                final Neuron n = new Neuron(fn, w);

                if (null != prev) {
                    for (Neuron p : prev.neurons) {
                        p.connect(n, w);
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

    public Network(Squash fn, int[] layers, double[][] weigths) {

        Layer prev = null;

        for (int l = 0; l < layers.length; l++) {

            final Neuron[] neurons = new Neuron[layers[l]];

            for (int n = layers[l]; n-- > 0; ) {

                final Neuron neuron = l == 0 ? new Neuron(fn, weigths[l][n])
                                             : new Neuron(fn);

                if (l > 0) {
                    for (int p = 0; p < prev.neurons.length; p++) {
                        neuron.connect(prev.neurons[p], weigths[l][n + p]);
                    }
                }

                neurons[n] = neuron;
            }

            if (l == 0) {
                prev = input = new Layer(neurons);
            }
            else {
                prev = new Layer(neurons, prev);
            }
        }

        output = prev;
    }

    public double[] execute(double ...x) {
        return input.input(x);
    }

    public double propagate(double η, double ...e) {
        return output.error(η, e);
    }
}
