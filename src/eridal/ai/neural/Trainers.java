package eridal.ai.neural;

import eridal.ai.math.Vector;


public abstract class Trainers {

    public static class GradientDescend extends Trainer {

        final Network network;

        public GradientDescend(Network network, double η) {
            super(η);
            this.network = network;
        }

        @Override public double go(double error) {

            double sum = 0;
            double avg = error + 1;

            double[] epoch = new double[(trainX.size() + testX.size()) * 15];

            for (int e = 0; error < avg; ) {

                for (int k = trainX.size(); k-- > 0; e++) {
                    double err = teach(network, trainX.get(k), trainY.get(k));
                    sum -= epoch[e + k];
                    sum += epoch[e + k] = err;
                }

                for (int k = testX.size(); k-- > 0; e++) {
                    double err = errorDiff(network, testX.get(k), testY.get(k));
                    sum -= epoch[e + k];
                    sum += epoch[e + k] = err;
                }

                avg = sum / trainX.size();

                if (e == epoch.length) {
                    double err = Vector.average(epoch);
                    if (err < ERROR_MIN) {
                        return err;
                    }
                    e = 0;
                }

            }

            return avg;
        }

        @Override
        public Network network() {
            return network;
        }
    }
}
