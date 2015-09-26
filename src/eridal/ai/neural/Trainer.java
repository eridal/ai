package eridal.ai.neural;

public abstract class Trainer {

    /**
     * Train the network until error drop.
     * 
     * @param network the network to train
     * @param η learn rate speed
     * @param inputs input values
     * @param outputs expected output values
     * @param e  minimum error threshold value
     * 
     * @return iterations required to train the network
     */
    public static long train(Network network,
                             double η,
                             double[][] inputs,
                             double[][] outputs,
                             double error) {

         long k = 0;
         double sum;

         do {
             sum = 0;
             for (int i = inputs.length; i-- >0; k += 1) {
                 double e = teach(network, η, inputs[i], outputs[i]);
                 sum += Math.abs(e);
             }
         } while (error < sum);

         return k;
     }

    public static long train(Network network,
                             double η,
                             double[][] inputs,
                             double[][] outputs) {
        return train(network, η, inputs, outputs, 0.0);
    }

    public static long train(Network network,
                             double[][] inputs,
                             double[][] outputs,
                             double e) {
        return train(network, 0.01, inputs, outputs, e);
    }

    public static long train(Network network,
                             double[][] inputs,
                             double[][] outputs) {
        return train(network, 0.01, inputs, outputs, 0.0);
    }

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
    public static double teach(Network net, double η, double[] x, double[] y) {

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
