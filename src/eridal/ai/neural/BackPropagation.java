package eridal.ai.neural;

import java.util.Arrays;

import eridal.ai.utils.ArrayMath;


public class BackPropagation {

    private final double bias;

    /** learn speed */
    private final double η;

    private double[]   y;
    private double[]   δ;
    private double[][] w;

    private int[] sizes;

    public BackPropagation(double learnSpeed, double bias) {
        this.η = learnSpeed;
        this.bias = bias;
        init(2, 3, 1);
    }

    public BackPropagation() {
        this(0.1, 0.3);
    }

    private void init(int ...layers) {

        sizes = new int[layers.length];
        sizes[0] = layers[0];

        for (int l = 1; l < layers.length; l++) {
            sizes[l] = layers[l] + sizes[l - 1];
        }

        int input = sizes[0];
        int output = layers[layers.length - 1];
        int total = ArrayMath.sum(layers);

        y = new double[total];
        δ = new double[total];
        w = new double[total - output][total];

        // input weights
        for (int i = input; i-- > 0;) {
            w[i][i] = 1.0 / input;
        }

        // connection weights
        for (int l = layers.length - 1; l-- > 0; ) {
            for (int i = l == 0 ? 0 : sizes[l - 1]; i < sizes[l]; i++) {
                for (int j = sizes[l + 1]; j-- > sizes[l];) {
                    w[i][j] = 1.0 / layers[l == 0 ? l + 1 : l];
                }
            }
        }
    }

    public boolean train(int[] x, int result) {
        double error = execute(x) - result;
        if (error != 0) {
            propagate(x, error);
        }
        return error != 0;
    }

    private void propagate(int[] x, double error) {

        boolean first = true;
        int from  = sizes.length - 1;
        int until = Math.max(0, sizes.length - 2);

        // back propagate
        for (int h = sizes[from]; h-- > sizes[until]; ) {

            double δe;

            if (first) {
                first = false;
                δe = error;
            } else {
                δe = δ(h, sizes[h - 1], sizes[h]);
            }

            δ[h] = y[h] * (1 - y[h]) * δe;

            for (int i = sizes[0]; i-- > 0; ){
                w[i][h] -= η * y[i] * δ[h];
            }
        }

        // input
        for (int i = sizes[0]; i-- > 0; ) {

            double δe = δ(i, sizes[0], sizes[1]);

            δ[i] = y[i] * (1 - y[i]) * δe;

            w[i][i] -= η * (double) x[i] * δ[i];
        }
    }

    private double δ(int n, int from, int to) {
        double δe = 0;
        for (int i = to; i-- > from;) {
            δe += δ[i] * w[n][i];
        }
        return δe;
    }

    private double[] calculate(int[] x) {

        // input
        for (int i = 0; i < sizes[0]; i++) {
            y[i] = net(x[i] * w[i][i]);
        }
        // feed forward
        for (int layer = 1; layer < sizes.length; layer++) {
            for (int h = 0; h < sizes[layer]; h++) {
                double sum = 0;
                for (int i = 0; i < sizes[layer - 1]; i++) {
                    sum += y[i] * w[i][h];
                }
                y[h] = net(sum);
            }
        }

        // output
        return Arrays.copyOfRange(y, sizes[sizes.length - 2], sizes[sizes.length - 1]);
    }

    private double net(double value) {
        return 1.0 / (1.0 + Math.exp(- value + bias));
    }

    public double execute(int[] x) {
        double[] y = calculate(x);
        return ArrayMath.sum(y) > 0.5 ? 1 : 0;
    }
}
