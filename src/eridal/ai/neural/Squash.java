package eridal.ai.neural;

public interface Squash {

    double activate(double x);

    double error(double y);
}
