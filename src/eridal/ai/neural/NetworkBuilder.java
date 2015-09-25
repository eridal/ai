package eridal.ai.neural;

public class NetworkBuilder {

    private Squash sq = Squashs.SIGMOID;
    private Threshold th = Thresholds.SPLIT;

    private int[] sizes;

    public NetworkBuilder squash(Squash sq) {
        this.sq = sq;
        return this;
    }

    public NetworkBuilder threshold(Threshold th) {
        this.th = th;
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

        return new NetworkImpl(in, out, th);
    }

    private Layer buildLayers() {

        Layer layer = null;
        Layer prev = null;
        int id = 0;

        for (int size : sizes) {

            Neuron[] neurons = new Neuron[size];

            while (size-- > 0) {

                final Neuron n = new Neuron(id++, sq);

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
            layer = new Layer(neurons, layer);
        }

        return layer;
    }

    
}
