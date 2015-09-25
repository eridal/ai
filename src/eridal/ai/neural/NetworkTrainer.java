package eridal.ai.neural;

public interface NetworkTrainer extends Network {

    /**
     * @param inputs
     * @param results
     * 
     * @param error stop training once error dropped this threshold
     * 
     * @return iteration required to train
     */
    public int train(double[][] inputs, double[][] results, double error);
}
