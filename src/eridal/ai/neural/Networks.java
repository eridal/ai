package eridal.ai.neural;

import eridal.ai.utils.Filter;

public class Networks {

    /**
     * Returns a Perceptron network.
     *
     * @param Θ activation bias
     * @return the created Perceptron
     */
    public static Network percepton(double Θ) {
        return new NetworkBuilder()
                .layers(2, 1)
                .bias(Θ)
                .squash(Squashs.IDENTITY)
                .filter(Filter.POSITIVE)
                .build();
    }

    /**
     * @param Θ activation bias
     * @param layers 
     * @return
     */
    public static Network backPropagation(double Θ, int...layers) {
        return new NetworkBuilder()
            .layers(layers)
            .bias(Θ)
            .squash(Squashs.SIGMOID)
            .filter(Filter.SPLIT)
            .build();
    }

}
