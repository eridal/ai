package eridal.ai.neural;

import java.util.Arrays;
import java.util.Iterator;

public class Layer implements Iterable<Neuron> {

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

    @Override public Iterator<Neuron> iterator() {
        return Arrays.stream(neurons).iterator();
    }

    public int level() {

        int level = 0;

        for (Layer layer = prev; null != layer; layer = layer.prev) {
            level += 1;
        }

        return level;
    }

    private static final char INPUT  = 'i';
    private static final char HIDDEN = 'h';
    private static final char OUTPUT = 'o';

    @Override public String toString() {
        final char type = null == prev ? INPUT : null == next ? OUTPUT : HIDDEN;
        return String.format("Layer(level:%d type:%c neurons:%d)", level(), type, neurons.length);
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
            throw new IllegalArgumentException();
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
            throw new IllegalArgumentException();
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

    public static class ForwardIterator implements Iterator<Layer> {

        private Layer layer;

        public ForwardIterator(Layer layer) {
            this.layer = layer;
        }

        @Override public boolean hasNext() {
            return null != layer;
        }

        @Override public Layer next() {
            Layer current = layer;
            layer = layer.next;
            return current;
        }
    }

    public static class BackwardsIterator implements Iterator<Layer> {

        private Layer layer;

        public BackwardsIterator(Layer layer) {
            this.layer = layer;
        }

        @Override public boolean hasNext() {
            return null != layer.prev;
        }

        @Override public Layer next() {
            return layer = layer.prev;
        }
    }
}
