package eridal.ai.neural;



public class BackPropagation {

    private final double bias;

    /** learn speed */
    private final double η;

    private double y0;
    private double y1;
    private double y2;
    private double y3;
    private double y4;
    private double y5;

    // input
    private double w0  = 0.5;
    private double w1  = 0.5;
    // hidden
    private double w02 = 0.3;
    private double w03 = 0.3;
    private double w04 = 0.3;
    private double w12 = 0.3;
    private double w13 = 0.3;
    private double w14 = 0.3;
    // output
    private double w25 = 0.3;
    private double w35 = 0.3;
    private double w45 = 0.3;

    public BackPropagation(double learnSpeed, double bias) {
        this.η = learnSpeed;
        this.bias = bias;
    }

    public BackPropagation() {
        this(0.1, 0.3);
    }

    public boolean train(int x0, int x1, int result) {
        double error = execute(x0, x1) - result;
        if (error != 0) {

            // output
            double δ5 = y5 * (1 - y5) * error;

            w45 -= η * y4 * δ5;
            w35 -= η * y3 * δ5;
            w25 -= η * y2 * δ5;

            // hidden
            double δ4 = y4 * (1 - y4) * (δ5 * w45);
            double δ3 = y3 * (1 - y3) * (δ5 * w35);
            double δ2 = y2 * (1 - y2) * (δ5 * w25);

            w14 -= η * y1 * δ4;
            w13 -= η * y1 * δ3;
            w12 -= η * y1 * δ2;

            w04 -= η * y0 * δ4;
            w03 -= η * y0 * δ3;
            w02 -= η * y0 * δ2;

            // input
            double δ1 = y1 * (1 - y1) * (δ4 + w14) *
                                        (δ3 + w13) *
                                        (δ2 + w12) ;
            double δ0 = y0 * (1 - y0) * (δ4 + w04) *
                                        (δ3 + w03) *
                                        (δ2 + w02) ;
            w1 -= η * x1 * δ1;
            w0 -= η * x0 * δ0;
        }
        return error != 0;
    }

    private double calculate(int x0, int x1) {
        // input
        y0 = net(x0 * w0 - bias);
        y1 = net(x1 * w1 - bias);
        // hidden
        y2 = net(y0 * w02 + y1 * w12 - bias);
        y3 = net(y0 * w03 + y1 * w13 - bias);
        y4 = net(y0 * w04 + y1 * w14 - bias);
        // output
        y5 = net(y2 * w25 +
                 y3 * w35 +
                 y4 * w45 - bias);
        // result
        return y5;
    }

    private double net(double value) {
        return 1.0 / (1.0 + Math.exp(-value));
    }

    public double execute(int x0, int x1) {
        double y = calculate(x0, x1);
        return y > 0.5 ? 1 : 0;
    }
}
