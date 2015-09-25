package eridal.ai.neural;

public abstract class Squashs {

    public static final Squash SIGMOID = new Squash() {

        @Override public double activate(double net) {
            return 1.0 / (1 + Math.exp(-net));
        }

        @Override public double error(double y) {
            return y * (1.0 - y);
        }
    };

    public static final Squash TANH = new Squash() {

        @Override public double activate(double net) {
            return Math.tanh(net);
        }

        @Override public double error(double y) {
            return 1 - Math.pow(Math.tanh(y), 2);
        }
    };
}
