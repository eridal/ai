package eridal.ai.neural;

import eridal.ai.utils.Filter;

class NetworkImpl implements Network {

    final Layer input;
    final Layer output;
    final Filter filter;

    public NetworkImpl(Layer input, Layer output, Filter filter) {
        this.input = input;
        this.output = output;
        this.filter = filter;
    }

    public double[] execute(double... x) {

        double[] y = input.input(x);

        if (null != filter) {
            for (int i = y.length; i-- > 0;) {
                y[i] = filter.apply(y[i]);
            }
        }

        return y;
    }

    public double propagate(double η, double... e) {
        return output.error(η, e);
    }

    @Override public Layer input() {
        return input;
    }

    @Override public Layer output() {
        return output;
    }
}