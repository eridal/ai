package eridal.ai.neural;


public class BackPropagation implements Network {

    /** learn speed */
    private final double η;

    /**
     * The network to train
     */
    private final Network net;

    /**
     * @param net The network to train
     * @param η learn speed
     */
    public BackPropagation(Network net, double η) {
        this.net = net;
        this.η = η;
    }

    /**
     * 
     * @param inputs
     * @param results
     * 
     * @param error stop training once error dropped this threshold
     * 
     * @return iteration required to train
     */
    public int train(double[][] inputs, double[][] results, double error) {

        int size = inputs.length * 10;
        int min = 2 * size;

        double[] e = new double[size];
        double sum = 0;
        double avg = 0;

        int k = 1;

        for (; k < min || error < avg; k++) {

            int i = k % inputs.length;
            int r = k % size;

            sum -= e[r];
            sum += e[r] = train(inputs[i], results[i]);

            avg = sum / k;
        }

        return k;
    }

    public double train(double[] inputs, double[] results) {

        double[] output = net.execute(inputs);

        if (output.length != results.length) {
            throw new IllegalArgumentException(); 
        }

        double[] delta = new double[results.length];

        for (int k = delta.length; k-- > 0; ) {
            delta[k] = output[k] - results[k];
        }

        return net.propagate(η, delta);
    }

    @Override public double[] execute(double... x) {
        return net.execute(x);
    }

    @Override public double propagate(double η, double... e) {
        return net.propagate(η, e);
    }
}

