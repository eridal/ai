package eridal.ai.neural;

import java.util.InputMismatchException;

public class BackPropagation2 implements Network {

    /** learn speed */
    private final double η;

    private final Network net;

    public BackPropagation2(Network net, double learnSpeed) {
        this.net = net;
        this.η = learnSpeed;
    }

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
            throw new InputMismatchException(); 
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

