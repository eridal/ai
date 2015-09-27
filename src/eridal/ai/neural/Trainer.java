package eridal.ai.neural;

import java.util.ArrayList;
import java.util.List;

import eridal.ai.utils.ArrayMath;

public class Trainer {

    final Network network;

    final double η;

    public List<double[]> trainX = new ArrayList<>();
    public List<double[]> trainY = new ArrayList<>();

    public List<double[]> testX = new ArrayList<>();
    public List<double[]> testY = new ArrayList<>();

    public Trainer(Network network, double η) {
        this.network = network;
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
    public double go(double error) {

        double sum = 0;
        double avg = error + 1;

        double[] epoch = new double[(trainX.size() + testX.size()) * 15];

        for (int e = 0; error < avg; ) {

            for (int k = trainX.size(); k-- > 0; e++) {
                double err = teach(trainX.get(k), trainY.get(k));
                sum -= epoch[e + k];
                sum += epoch[e + k] = err;
            }

            for (int k = testX.size(); k-- > 0; e++) {
                double err = errorDiff(testX.get(k), testY.get(k));
                sum -= epoch[e + k];
                sum += epoch[e + k] = err;
            }

            avg = sum / trainX.size();

            if (e == epoch.length) {
                double err = ArrayMath.sum(epoch) / epoch.length;
                if (err < ERROR_MIN) {
                    return err;
                }
                e = 0;
            }

        }

        return avg;
    }

    /**
     * Teach a given set of inputs to some network
     *
     * @param x input values
     * @param y expected output values
     *
     * @return error delta
     */
    private double teach(double[] x, double[] y) {
        double[] err = errorVector(x, y);
        return network.propagate(η, err);
    }

    private double[] errorVector(double[] x, double[] y) {

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

    private double errorDiff(double[] x, double[] y) {
        double[] err = errorVector(x, y);
        double r = 0;
        for (double e : err) {
            r += e;
        }
        return r;
    }

}
