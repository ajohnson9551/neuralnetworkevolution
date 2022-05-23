package neuralevolution;

import java.util.Random;

public class Utility {

    private static Utility utility;
    private static double[] sigmoidValues;
    private static final int numApproximations = 10000;
    private static final int sigmoidApproxRange = 10;
    private static Random rand = new Random();

    private Utility() {
        sigmoidValues = new double[numApproximations];
        for (int i = 0; i < numApproximations; i++) {
            sigmoidValues[i] = sigmoid(2.0 * sigmoidApproxRange * (i - numApproximations/2.0) / numApproximations);
        }
    }

    public static Utility getUtility() {
        if (utility == null) {
            utility = new Utility();
        }
        return utility;
    }

    public double randVal(double range) {
        return range * (2.0*rand.nextDouble() - 1.0);
    }

    public String roundString(double x) {
        String r = x >= 0 ? " " : "";
        r += String.format("%.9f", x);
        return r;
    }

    public double tweakValue(double x, double range){
        double y = x + randVal(range);
//        if (y > 1) {
//            return 1;
//        } else if (y < -1) {
//            return -1;
//        }
        return y;
    }

    public void sigmoid(double[] input) {
        for (int i = 0; i < input.length; i++) {
            input[i] = fastSigmoid(input[i]);
        }
    }

    public double sigmoid(double x) {
        return (2 * Math.exp(x) / (1 + Math.exp(x))) - 1;
    }

    public int indexOfApprox(double x) {
        if (x < -1 * sigmoidApproxRange) {
            return 0;
        } else if (x > sigmoidApproxRange) {
            return numApproximations - 1;
        } else {
            return (int) (numApproximations * ((x + sigmoidApproxRange) / (2.0 * sigmoidApproxRange)));
        }
    }

    public double fastSigmoid(double x) {
        return sigmoidValues[indexOfApprox(x)];
    }

    public double[] evaluate(double[][] A, double[] b, double[] x, boolean doSigmoid) {
        double[] y = new double[A.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                y[i] += A[i][j]*x[j];
            }
            y[i] += b[i];
        }
        if (doSigmoid) {
            sigmoid(y);
        }
        return y;
    }

    public int maxIndex(double[] x) {
        int i = 0;
        double max = x[0];
        for (int j = 0; j < x.length; j++) {
            if (x[j] > max) {
                max = x[j];
                i = j;
            }
        }
        return i;
    }

    public int indexRank(double[] x, int i) {
        double m = x[i];
        int rank = 0;
        for (int j = 0; j < x.length; j++) {
            if (x[j] > m) {
                rank++;
            }
        }
        return rank;
    }

    public void normalize(double[] x) {
        double mag = 0.0;
        for (double d : x) {
            mag += d * d;
        }
        mag = Math.sqrt(mag);
        for (int i = 0; i < x.length; i++) {
            x[i] = x[i] / mag;
        }
    }

    public double mse(double[] guess, int correct) {
        double e = 0;
        for (int i = 0; i < guess.length; i++) {
            if (i != correct) {
                e += guess[i] * guess[i];
            } else {
                e += (1 - guess[i]) * (1 - guess[i]);
            }
        }
        return (e / ((double) guess.length));
    }
}
