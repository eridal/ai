package eridal.ai.neural;

import java.util.Iterator;

import eridal.ai.utils.Filter;
import eridal.ai.utils.Nulls;

public class Network implements Iterable<Layer> {

    final Layer input;
    final Layer output;

    final Filter filter;

    public Network(Layer input, Layer output, Filter filter) {
        this.input = input;
        this.output = output;
        this.filter = Nulls.coalesce(filter, Filter.NONE);
    }

    public Network(Layer input, Layer output) {
        this(input, output, null);
    }

    public double[][] matrix() {

        int total = 0;

        for (Layer layer = input; null != layer; layer = layer.next()) {
            for (@SuppressWarnings("unused") Neuron n : layer) {
                total += 1;
            }
        }

        double[][] matrix = new double[total][total];

        for (Layer layer = input; null != layer; layer = layer.next()) {
            for (Neuron n : layer) {
                matrix[n.id()][n.id()] = n.bias();
                for (Synapse s : n.targets()) {
                    matrix[n.id()][s.target().id()] = s.weight();
                }
            }
        }

        return matrix;
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
        return filter.apply(output.result());
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

    public int[] layersSizes() {

        int k = output.level();
        int[] layers = new int[k];

        Layer layer = output;

        while (k--> 0) {
            layers[k] = layer.size();
            layer = layer.prev();
        }

        return layers;
    }

    /**
     * Returns an iterator over the layers in this network, 
     * from the output, until the input layer.
     */
    public Iterator<Layer> layersReverse() {
        return new Layer.BackwardsIterator(output);
    }

}
