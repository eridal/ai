package eridal.ai.neural;

import eridal.ai.utils.Filter;

public class NetworkBuilder {

    private Squash sq = Squashs.SIGMOID;
    private Filter filter;

    private double Θ = 0;
    private int[] sizes;

    public NetworkBuilder squash(Squash sq) {
        this.sq = sq;
        return this;
    }

    public NetworkBuilder filter(Filter filter) {
        this.filter = filter;
        return this;
    }

    public NetworkBuilder bias(double Θ) {
        this.Θ = Θ;
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

        return new NetworkImpl(in, out, filter);
    }

    private Layer buildLayers() {

        Layer layer = null;
        Layer prev = null;
        int id = 0;

        for (int size : sizes) {

            Neuron[] neurons = new Neuron[size];

            while (size-- > 0) {

                final Neuron n = new Neuron(id++, Θ, sq);

                if (null != prev) {
                    for (Neuron p : prev.neurons) {
                        p.connect(n, Math.random() * 2.0 - 1.0);
                    }
                }

                neurons[size] = n;
            }

            if (null == layer) {
                layer = new Layer(neurons);
            }
            else {
                layer = new Layer(neurons, layer);
            }

            prev = layer;
        }

        return layer;
    }

    
}
