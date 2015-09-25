package eridal.ai.neural;

import java.util.Iterator;


public interface Network extends Iterable<Layer> {

    /**
     * Feed forward the network with the given input.
     *
     * @param x values to be feed to the input layer
     * @return output
     *
     * @throws IllegalArgumentException when the input {@code x}
     *      does not match the input's layer size
     */
    public double[] execute(double ...x);

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
    public double propagate(double η, double ...e);

    /**
     * Returns this network's input layer
     */
    public Layer input();

    /**
     * Returns this network's output layer
     */
    public Layer output();

    /**
     * Returns an iterator over the layers in this network, 
     * from the input, until the output layer.
     */
    public default Iterator<Layer> iterator() {
        return layersForward();
    }

    /**
     * Returns an iterator over the layers in this network, 
     * from the input, until the output layer.
     */
    public default Iterator<Layer> layersForward() {
        return new Layer.ForwardIterator(input());
    }

    /**
     * Returns an iterator over the layers in this network, 
     * from the output, until the input layer.
     */
    public default Iterator<Layer> layersBackwards() {
        return new Layer.BackwardsIterator(input());
    }
}
