package eridal.ai.neural;

import java.util.InputMismatchException;

public class Layer {

    final Neuron[] neurons;
    final double[] y;

    Layer prev;
    Layer next;

    public Layer(Neuron[] neurons) {
        this.neurons = neurons;
        y = new double[neurons.length];
    }

    public Layer(Neuron[] neurons, Layer prev) {
        this(neurons);
        this.prev = prev;
        prev.next = this;
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

        for (int k = neurons.length; k-- > 0; ) {
            y[k] = neurons[k].input(x[k]);
        }

        double[] result = y;

        // feed forward

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
}
