package eridal.ai.math;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

public abstract class Matrix {

    private Matrix() {
        throw new UnsupportedOperationException();
    }

    public static double[][] identity(int size) {
        return identity(size, 1.0);
    }

    /**
     * @return <pre>Identity * k</pre>
     */
    public static double[][] identity(int size, double k) {

        if (size < 0) {
            throw new IllegalArgumentException("Negative matrix size");
        }

        double[][] M = new double[size][size];

        for (int i = 0; i < size; i++) {
            M[i][i] = k;
        }

        return M;
    }

    /**
     * @param M matrix
     *
     * @return <pre>M<sup>t</sup></pre>
     */
    public static double[][] transpose(double[][] M) {

        int rows = M.length;
        int cols = M[0].length;

        if (rows != cols) {
            throw new IllegalArgumentException("matrix is not square");
        }

        double[][] T = new double[rows][cols];

        for (int r = rows; r-- > 0; ) {
            for (int c = cols; c-- > 0; ) {
                T[r][c] = M[c][r];
            }
        }

        return T;
    }

    public static double[][] apply(double[][] M, DoubleUnaryOperator op) {

        double[][] R = new double[M.length][M[0].length];

        for (int row = M.length; row-- > 0; ) {
            for (int col = M[row].length; col-- > 0; ) {
                R[row][col] = op.applyAsDouble(M[row][col]);
            }
        }

        return R;
    }

    public static double[][] apply(double[][] A, double[][] B, DoubleBinaryOperator op) {

        double[][] r = new double[A.length][A[0].length];

        for (int row = A.length; row-- > 0; ) {
            for (int col = A[row].length; col-- > 0; ) {
                r[row][col] = op.applyAsDouble(A[row][col], B[row][col]);
            }
        }

        return r;
    }

    /**
     * @param M matrix
     * @param k scalar
     *
     * @return <pre>M + k</pre>
     */
    public static double[][] add(double[][] M, double k) {
        return apply(M, x -> x + k);
    }

    /**
     * @param A matrix
     * @param B matrix
     *
     * @return <pre>A + B</pre>
     */
    public static double[][] add(double[][] A, double[][] B) {
        return apply(A, B, (x, y) -> x + y);
    }

    /**
     * @param A matrix
     * @param B matrix
     *
     * @return <pre>A - B</pre>
     */
    public static double[][] substract(double[][] A, double[][] B) {
        return apply(A, B, (x, y) -> x - y);
    }

    /**
     * @param M matrix
     * @param k scalar
     *
     * @return <pre>M - k</pre>
     */
    public static double[][] substract(double[][] M, double k) {
        return apply(M, x -> x - k);
    }

    /**
     * @param k scalar
     * @param M matrix
     *
     * @return <pre>k - M</pre>
     */
    public static double[][] substract(double k, double[][] M) {
        return apply(M, x -> k - x);
    }

    /**
     * @param M matrix
     * @param k scalar
     *
     * @return <pre>k * M</pre>
     */
    public static double[][] product(double[][] M, double k) {
        return apply(M, x -> x * k);
    }

    /**
     * @param A matrix
     * @param B matrix
     *
     * @return <pre>R = A * B</pre>
     */
    public static double[][] product(double[][] A, double[][] B) {

        if (A[0].length != B.length) {
            throw new IllegalArgumentException("Non-compatibles matrix sizes");
        }

        int rows = A.length;
        int cols = B[0].length;

        double[][] P = new double[rows][cols];

        for (int r = rows; r-- > 0; ) {
            for (int c = cols; c-- > 0; ) {
                for (int k = A[0].length; k-- > 0; ) {
                    P[r][c] += A[r][k] * B[k][c];
                }
            }
        }

        return P;
    }

    /**
     * @param v1 vector
     * @param v2 vector
     *
     * @return <pre>M = v1 * v2</pre>
     *
     * @throws IllegalArgumentException if vectors' dimensions are different
     */
    public static double[][] product(double[] v1, double[] v2) {

        if (v1.length != v2.length) {
            throw new IllegalArgumentException();
        }

        double[][] M = new double[v1.length][v2.length];

        for (int i = v1.length; i-- > 0; ) {
            for (int j = v2.length; j-- > 0; ) {
                M[i][j] = v1[i] * v2[j];
            }
        }

        return M;
    }

   /**
    * @param M matrix
    * @param exp exponent >= 0
    *
    * @return <pre>R = M<sup>(exp - 1)</sup> * M</pre>
    */
   public static double[][] pow(double[][] M, int exp) {

       if (M.length != M[0].length) {
           throw new IllegalArgumentException("Matrix is not square");
       }

       if (exp == 0) {
           return identity(M.length);
       }

       double[][] R = M;

       for (int i = 1; i < exp; i++) {
           R = product(R, M);
       }

       return R;
   }

}
