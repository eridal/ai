package eridal.ai.neural;

public class Perceptron {

    private double weightX;
    private double weightY;

    /** Activation threshold */
    public final double bias;
    public final double learnSpeed;

    public Perceptron(double weightX,
                      double weightY,
                      double bias,
                      double learnSpeed) {
        this.weightX = weightX;
        this.weightY = weightY;
        this.bias = bias;
        this.learnSpeed = learnSpeed;
    }

    public Perceptron(double bias,
                      double learnSpeed) {
        this(0.5, 0.5, bias, learnSpeed);
    }

    public Perceptron() {
        this(0.5, 0.5, 0.3, 0.1);
    }

    /**
     * Trains the model
     * 
     * @return a boolean indicating if the model have just learn something new 
     */
    public boolean train(int x, int y, int result) {

        double error = classify(x, y) - Integer.signum(result);

        if (error != 0) {
            weightX -= error * learnSpeed * x;
            weightY -= error * learnSpeed * y;
        }

        return error != 0;
    }

    public int classify(int x, int y) {
        double net = x * weightX +
                     y * weightY ;
        return net - bias > 0 ? 1 : 0;
    }

}
