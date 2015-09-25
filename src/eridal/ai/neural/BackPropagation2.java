package eridal.ai.neural;

import java.util.InputMismatchException;

public class BackPropagation2 {

    /** learn speed */
    private final double η;

    private final Network net;

    public BackPropagation2(Network net, double learnSpeed) {
        this.net = net;
        this.η = learnSpeed;
    }

    public void train(double[][] inputs, double[][] results, double error) {

        int i = 0;
        double err;

        do {
            i += 1;
            i %= inputs.length;
            err = train(inputs[i], results[i]);
        } while (error < Math.abs(err));
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
}

