package eridal.ai.neural;

import java.util.InputMismatchException;

public class Layer {

    final Neuron[] neurons;

    final double[] y;

    private Layer prev;
    private Layer next;

    public Layer(Neuron[] neurons) {
        this.neurons = neurons;
        y = new double[neurons.length];
    }

    public Layer(Neuron[] neurons, Layer prev) {
        this(neurons);
        this.prev = prev;
        prev.next = this;
    }

    public Layer prev() {
        return prev;
    }

    public Layer next() {
        return next;
    }

    public boolean isInput() {
        return null == prev;
    }

    public boolean isOutput() {
        return null == next;
    }

    public double[] input(double ...x) {

        if (neurons.length != x.length) {
            throw new InputMismatchException();
        }

        // set inputs
        for (int k = neurons.length; k-- > 0; ) {
            y[k] = neurons[k].input(x[k]);
        }

        double[] result = y;

        // and feed forward
        for (Layer layer = next; null != layer; layer = layer.next) {
            result = layer.activate();
        }

        return result;
    }

    private double[] activate() {
        for (int k = neurons.length; k-- > 0; ) {
            y[k] = neurons[k].activate();
        }
        return y;
    }

    public double error(double η, double[] e) {

        if (neurons.length != e.length) {
            throw new InputMismatchException();
        }

        double δe = 0;

        // calculate errors
        for (int k = neurons.length; k-- > 0; ) {
            δe += neurons[k].error(η, e[k]);
        }

        // propagate backwards
        for (Layer layer = prev; null != layer; layer = layer.prev) {
            δe += layer.propagate(η);
        }

        return δe;
    }

    public double propagate(double η) {

        double δe = 0;

        for (int k = neurons.length; k-- > 0; ) {
            δe += neurons[k].propagate(η);
        }

        return δe;
    }
}
