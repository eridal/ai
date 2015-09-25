package eridal.ai.neural;

import eridal.ai.utils.Filter;

public class NetworkBuilder {

    private Squash sq = Squashs.SIGMOID;
    private Filter filter;

    private double bias = 0;
    private int[] sizes;

    public NetworkBuilder squash(Squash sq) {
        this.sq = sq;
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

    public NetworkBuilder layers(int... sizes) {
        this.sizes = sizes;
        return this;
    }

    public Network build() {

        Layer out = buildLayers();
        Layer in = out;

        for (Layer layer = out; null != layer; layer = layer.prev()) {
            in = layer;
        }

        return new Network(in, out, filter);
    }

    private Layer buildLayers() {

        Layer layer = null;
        Layer prev = null;
        int id = 0;

        for (int size : sizes) {

            Neuron[] neurons = new Neuron[size];

            while (size-- > 0) {

                final Neuron n = new Neuron(id++, bias, sq);

                if (null != prev) {
                    for (Neuron p : prev.neurons) {
                        Synapse.plug(p, n);
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

        return layer;
    }

    
}
