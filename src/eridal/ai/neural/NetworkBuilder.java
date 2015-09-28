package eridal.ai.neural;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;

import eridal.ai.utils.Filter;

public class NetworkBuilder {

    private Squash squash = Squashs.SIGMOID;
    private Filter filter;

    private Double bias = null;

    private int[] sizes;
    private double[][] matrix;

    public NetworkBuilder squash(Squash squash) {
        this.squash = squash;
        return this;
    }

    public Squash squash() {
        return squash;
    }

    public NetworkBuilder filter(Filter filter) {
        this.filter = filter;
        return this;
    }

    public Filter filter() {
        return filter;
    }

    public NetworkBuilder bias(double bias) {
        this.bias = bias;
        return this;
    }

    public double bias() {
        return bias;
    }

    public NetworkBuilder matrix(double[][] matrix) {
        this.matrix = matrix;
        return this;
    }

    public double[][] matrix() {
        return matrix;
    }

    public NetworkBuilder layers(int... sizes) {
        this.sizes = sizes;
        return this;
    }

    public int[] layers() {
        return sizes;
    }

    public Network build() {

        Layer layer = null;
        Layer prev = null;

        int id = 0;

        for (int size : sizes) {

            Neuron[] neurons = new Neuron[size];

            while (size-- > 0) {

                final Neuron n = new Neuron(id++, squash);

                if (null != prev) {

                    if (null != matrix) {
                        n.bias(matrix[n.id()][n.id()]);
                    }
                    else if (bias != null) {
                        n.bias(bias);
                    }

                    for (Neuron p : prev) {
                        if (null != matrix) {
                            p.synapseTo(n, matrix[p.id()][n.id()]);
                        }
                        else {
                            p.synapseTo(n);
                        }
                    }
                }

                neurons[size] = n;
            }

            if (null == layer) {
                layer = new Layer(neurons);
            }
            else {
                layer = layer.createNext(neurons);
            }

            prev = layer;
        }

        final Layer in = Layer.first(layer);
        return new Network(in, layer, filter);
    }

    public static void write(String fileName, Network network) throws IOException {
        final File file = new File(fileName);
        write(file, network);
    }

    public static void write(File file, Network network) throws IOException {
        try (final Writer w = new FileWriter(file)) {
            writeLayers(w, network.layers());
            writeMatrix(w, network.matrix());
            w.flush();
        }

    }

    public static void writeLayers(Writer w, Iterator<Layer> iterator) throws IOException {
        boolean first = true;
        while (iterator.hasNext()) {
            final Layer layer = iterator.next();
            if (first) {
                first = false;
            } 
            else {
                w.write(' ');
            }
            w.write(String.valueOf(layer.size()));
        }
        w.write('\n');
    }

    public static void writeMatrix(Writer w, double[][] matrix) throws IOException {

        w.write(String.valueOf(matrix.length));
        w.write(' ');
        w.write(String.valueOf(matrix[0].length));
        w.write('\n');

        for (double[] row : matrix) {
            boolean first = true;
            for (double v : row) {
                if (first) {
                    first = false;
                }
                else {
                    w.write(' ');
                }
                w.write(String.valueOf(v));
            }
            w.write('\n');
        }
    }

    public static NetworkBuilder read(String fileName) throws IOException {
        final File file = new File(fileName);
        return read(file);
    }

    public static NetworkBuilder read(File file) throws IOException {

        final int[] sizes;
        final double[][] matrix;

        try (Scanner s = new Scanner(file)) {
            s.useLocale(Locale.ENGLISH);
            sizes = scanLayers(s);
            matrix = scanMatrix(s);
        }

        return new NetworkBuilder()
                .layers(sizes)
                .matrix(matrix);
    }

    private static int[] scanLayers(Scanner s) throws IOException {

        String[] lines = s.nextLine().split(" ");
        int[] layers = new int[lines.length];

        for (int k = lines.length; k-- > 0; ) {
            layers[k] = Integer.valueOf(lines[k]);
        }

        return layers;
    }

    private static double[][] scanMatrix(Scanner s) throws IOException {

        int rows = s.nextInt();
        int cols = s.nextInt();

        s.nextLine();

        double[][] matrix = new double[rows][cols];

        for (int r = 0; r < rows; r++) {
            String[] d = s.nextLine().split(" ");
            for (int c = 0; c < cols; c++) {
                matrix[r][c] = Double.valueOf(d[c]);
            }
        }

        return matrix;
    }
}
