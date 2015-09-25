package eridal.ai.neural;


class NetworkImpl implements Network {

    final Layer input;
    final Layer output;
    final Threshold th;

    public NetworkImpl(Layer input, Layer output, Threshold th) {
        this.input = input;
        this.output = output;
        this.th = th;
    }

    public double[] execute(double... x) {

        double[] y = input.input(x);

        if (null != th) {
            for (int i = y.length; i-- > 0;) {
                y[i] = th.apply(y[i]);
            }
        }

        return y;
    }

    public double propagate(double η, double... e) {
        return output.error(η, e);
    }
}