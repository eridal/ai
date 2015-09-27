package eridal.ai.neural;

public enum Squashs implements Squash {

    IDENTITY () {

        @Override public double activate(double x) {
            return x;
        }

        @Override public double error(double y) {
            return 1;
        }
    },

    SIGMOID () {

        @Override public double activate(double x) {
            return 1.0 / (1.0 + Math.exp(-x));
        }

        @Override public double error(double y) {
            return y * (1.0 - y);
        }
    },

    TANH () {

        @Override public double activate(double x) {
            return Math.tanh(x);
        }

        @Override public double error(double y) {
            return 1 - Math.pow(Math.tanh(y), 2);
        }
    },

    ;
}
