package eridal.ai.neural;

import java.util.Iterator;

import eridal.ai.utils.Filter;

public class Network implements Iterable<Layer> {

    final Layer input;
    final Layer output;
    final Filter filter;

    public Network(Layer input, Layer output, Filter filter) {
        this.input = input;
        this.output = output;
        this.filter = Filter.optional(filter);
    }

    /**
     * Feed forward the network with the given input.
     *
     * @param x values to be feed to the input layer
     * @return output
     *
     * @throws IllegalArgumentException when the input {@code x}
     *      does not match the input's layer size
     */
    public double[] execute(double ...x) {
        double[] y = input.input(x);
        return filter.apply(y);
    }

    /**
     * Returns the output
     */
    public double[] result() {
        return filter.apply(output.y);
    }

    /**
     * Propagate errors backwards, from the output layer,
     * until the input layer.
     *
     * @param η learn speed
     * @param e delta errors, one for each output neuron
     *
     * @return error delta
     *
     * @throws IllegalArgumentException when the input {@code δ}
     *      does not match the output's layer size
     */
    public double propagate(double η, double... e) {
        return output.error(η, e);
    }

    /**
     * Returns this network's input layer
     */
    public Layer input() {
        return input;
    }

    /**
     * Returns this network's output layer
     */
    public Layer output() {
        return output;
    }

    /**
     * Returns an iterator over the layers in this network, 
     * from the input, until the output layer.
     */
    public Iterator<Layer> iterator() {
        return layers();
    }

    /**
     * Returns an iterator over the layers in this network, 
     * from the input, until the output layer.
     */
    public Iterator<Layer> layers() {
        return new Layer.ForwardIterator(input);
    }

    /**
     * Returns an iterator over the layers in this network, 
     * from the output, until the input layer.
     */
    public Iterator<Layer> layersReverse() {
        return new Layer.BackwardsIterator(output);
    }
}
