package eridal.ai.neural;

public interface Squash {

    double activate(double net);

    double error(double y);
}
