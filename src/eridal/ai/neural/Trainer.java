package eridal.ai.neural;

import java.util.ArrayList;
import java.util.List;

public abstract class Trainer {

    public List<double[]> trainX = new ArrayList<>();
    public List<double[]> trainY = new ArrayList<>();

    public List<double[]> testX = new ArrayList<>();
    public List<double[]> testY = new ArrayList<>();

    final double η;

    public Trainer(double η) {
        this.η = η;
    }

    public Trainer train(double[] x, double[] y) {
        this.trainX.add(x);
        this.trainY.add(y);
        return this;
    }

    public Trainer test(double[] x, double[] y) {
        this.testX.add(x);
        this.testY.add(y);
        return this;
    }

    static final double ERROR_MIN = 1e-7;

    /**
     * Train the network until error drop to minimum.
     * 
     * @param error minimum error threshold value
     * 
     * @return error rate
     */
    public double go() {
        return go(-1);
    }

    /**
     * Train the network until error drop,
     * or it wont get any better.
     * 
     * @param error minimum error threshold value
     * 
     * @return error rate
     */
    public abstract double go(double error);

    /** 
     * @return The trained network
     */
    public abstract Network network();

    /**
     * Teach a given set of inputs to some network
     *
     * @param x input values
     * @param y expected output values
     *
     * @return error delta
     */
    protected double teach(Network network, double[] x, double[] y) {
        double[] err = errorVector(network, x, y);
        double e = network.propagate(η, err);
        return Math.abs(e);
    }

    protected double[] errorVector(Network network, double[] x, double[] y) {

        double[] r = network.execute(x);

        if (r.length != y.length) {
            throw new IllegalArgumentException();
        }

        double[] err = new double[y.length];

        for (int k = y.length; k-- > 0;) {
            err[k] = r[k] - y[k];
        }

        return err;
    }

    protected double errorDiff(Network network, double[] x, double[] y) {
        double[] err = errorVector(network, x, y);
        double r = 0;
        for (double e : err) {
            r += Math.abs(e);
        }
        return r;
    }

    protected double error(Network network) {
        return errorTrain(network) + errorTest(network);
    }

    protected double errorTrain(Network network) {
        return error(network, trainX, trainY);
    }

    protected double errorTest(Network network) {
        return error(network, testX, testY);
    }

    private double error(Network network, List<double[]> x, List<double[]> y) {

        int size = 0;
        double sum = 0;

        for (int k = x.size(); k-- > 0; size++) {
            sum += errorDiff(network, x.get(k), y.get(k));
        }

        return size > 0 ? sum / size : 0;
    }
}
