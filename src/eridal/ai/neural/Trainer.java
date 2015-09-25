package eridal.ai.neural;

public interface Trainer {

    /**
     * @param network the network to train
     * @param η learn rate speed
     * @param inputs input values
     * @param outputs expected output values
     * @param e  minimum error threshold value
     * 
     * @return iterations required to train the network
     */
    public int train(Network network,
                     double η,
                     double[][] inputs,
                     double[][] outputs,
                     double e);

    /**
     * Train the network until error drop.
     */
    public static final Trainer UNTIL = (net, η, inputs, results, error) -> {

        int k = 0;
        double sum;

        do {
            sum = 0;
            for (int i = inputs.length; i-- >0; k++) {
                double e = teach(net, η, inputs[i], results[i]);
                sum += Math.abs(e);
            }
        } while (error < sum);

        return k;
    };

    /**
     * Teach a given set of inputs to some network 
     *
     * @param net The network to be trained
     *
     * @param η learn speed rate
     * @param x input values
     * @param y expected output values
     *
     * @return error rate delta
     */
    static double teach(Network net, double η, double[] x, double[] y) {

        double[] r = net.execute(x);

        if (r.length != y.length) {
            throw new IllegalArgumentException();
        }

        double[] Δerr = new double[y.length];

        for (int k = y.length; k-- > 0;) {
            Δerr[k] = r[k] - y[k];
        }

        return net.propagate(η, Δerr);
    }
}
