package eridal.ai.neural;

import eridal.ai.utils.Filter;

public class NetworkBuilder {

    private Squash squash = Squashs.SIGMOID;
    private Filter filter;

    private Double bias = null;

    private int[] sizes;
    private double[][] matrix;

    public NetworkBuilder squash(Squash squash) {
        this.squash = squash;
        return this;
    }

    public NetworkBuilder filter(Filter filter) {
        this.filter = filter;
        return this;
    }

    public NetworkBuilder bias(double bias) {
        this.bias = bias;
        return this;
    }

    public NetworkBuilder matrix(double[][] matrix) {
        this.matrix = matrix;
        return this;
    }

    public NetworkBuilder layers(int... sizes) {
        this.sizes = sizes;
        return this;
    }

    public Network build() {

        Layer layer = null;
        Layer prev = null;

        int id = 0;

        for (int size : sizes) {

            Neuron[] neurons = new Neuron[size];

            while (size-- > 0) {

                final Neuron n = new Neuron(id++, squash);

                if (null != prev) {

                    if (null != matrix) {
                        n.bias(matrix[n.id()][n.id()]);
                    }
                    else if (bias != null) {
                        n.bias(bias);
                    }

                    for (Neuron p : prev.neurons) {
                        if (null != matrix) {
                            p.synapseTo(n, matrix[p.id()][n.id()]);
                        }
                        else {
                            p.synapseTo(n);
                        }
                    }
                }

                neurons[size] = n;
            }

            if (null == layer) {
                layer = new Layer(neurons);
            }
            else {
                layer = layer.createNext(neurons);
            }

            prev = layer;
        }

        final Layer in = Layer.first(layer);
        return new Network(in, layer, filter);
    }
}
