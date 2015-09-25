package eridal.ai.neural;

public interface Network {

    public double[] execute(double ...x);

    public double propagate(double η, double ...e);
}
