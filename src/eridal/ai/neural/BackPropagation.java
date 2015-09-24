package eridal.ai.neural;

import java.util.Arrays;

import eridal.ai.utils.ArrayMath;


public class BackPropagation {

    private final double bias;

    /** learn speed */
    private final double η;

    /** neuron outputs */
    private final double[] y;

    /** errors */
    private final double[] δ;

    /** connection weights */
    private final double[][] w;

    /** layer sizes */
    private final int[] layersSize;

    /** layer sizes */
    private final int[] layersMax;

    public BackPropagation(double learnSpeed, double bias, int[] layers) {

        this.η = learnSpeed;
        this.bias = bias;

        layersSize = layers;

        layersMax = new int[layers.length];
        layersMax[0] = layers[0];

        for (int i = 1; i < layers.length; i++) {
            layersMax[i] = layersMax[i - 1] + layers[i];
        }

        int output = layersSize[layers.length - 1];
        int total  = layersMax [layers.length - 1];

        y = new double[total];
        δ = new double[total];
        w = new double[total - output][total];

        // input weights
        for (int i = layersMax[0]; i-- > 0;) {
            w[i][i] = 1.0 / layersMax[0];
        }

        // default connection weights
        for (int l = layersMax.length - 1; l-- > 0; ) {
            for (int i = l == 0 ? 0 : layersMax[l - 1]; i < layersMax[l]; i++) {
                for (int j = layersMax[l + 1]; j-- > layersMax[l];) {
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

        // back propagate until layer 1
        for (int l = layersSize.length; l-- > 1;) {

            int kMin = layersMax[l - 1] - layersSize[l - 1];
            int kMax = layersMax[l - 0] - layersSize[l - 0];

            for (int n = layersMax[l]; n-- > layersMax[l - 1]; ) {

                double δn;

                if (first) {
                    δn = error;
                } else {
                    δn = δ(n, l + 1);
                }

                δ[n] = y[n] * (1 - y[n]) * δn;

                for (int k = kMax; k-- > kMin; ){
                    w[k][n] -= η * y[k] * δ[n];
                }
            }

            first = false;
        }

        // input
        for (int n = layersMax[0]; n-- > 0; ) {

            double δn = δ(n, 1);

            δ[n] = y[n] * (1 - y[n]) * δn;

            w[n][n] -= η * (double) x[n] * δ[n];
        }
    }

    private double δ(int n, int l) {

        int min = layersMax[l - 1];
        int max = layersMax[l];

        double δn = 0;

        for (int k = max; k-- > min;) {
            δn += δ[k] * w[n][k];
        }

        return δn;
    }

    private double[] calculate(int[] x) {

        // input
        for (int i = 0; i < layersMax[0]; i++) {
            y[i] = net(x[i] * w[i][i]);
        }

        // feed forward
        for (int l = 1; l < layersMax.length; l++) {

            int min = layersMax[l - 1] - layersSize[l - 1];
            int max = layersMax[l - 0] - layersSize[l - 0];

            for (int n = layersMax[l - 1]; n < layersMax[l]; n++) {
                double sum = 0;
                for (int k = min; k < max; k++) {
                    sum += y[k] * w[k][n];
                }
                y[n] = net(sum);
            }
        }

        // output the last layer
        int i = layersMax[layersMax.length - 2];
        int j = layersMax[layersMax.length - 1];

        return Arrays.copyOfRange(y, i, j);
    }

    private double net(double value) {
        return 1.0 / (1.0 + Math.exp(- value + bias));
    }

    public double execute(int[] x) {
        double[] y = calculate(x);
        return ArrayMath.sum(y) > 0.5 ? 1 : 0;
    }
}
