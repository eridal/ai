package eridal.ai.neural;

public abstract class NetworkWrapper implements Network {

    /**
     * The wrapper network to train
     */
    final Network net;

    NetworkWrapper(Network net) {
        this.net = net;
    }

    @Override public double[] execute(double... x) {
        return net.execute(x);
    }

    @Override public double propagate(double η, double... e) {
        return net.propagate(η, e);
    }

    @Override public Layer input() {
        return net.input();
    }

    @Override public Layer output() {
        return net.output();
    }

}
