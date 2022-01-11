package neuralevolution;

import java.util.Random;

public class LayeredNetworkParameters extends NetworkParameters {

    protected int[] hiddenLayerSizes;

    public LayeredNetworkParameters(int[] hiddenLayerSizes, int numInputs, int numOutputs) {
        super(numInputs, numOutputs);
        this.hiddenLayerSizes = hiddenLayerSizes;
    }

    public double[][][] setupWeights() {
        int l = hiddenLayerSizes.length;
        double[][][] weights = new double[l + 1][][];
        if (l == 0) {
            weights[0] = initialMatrix(numOutputs, numInputs);
        } else {
            weights[0] = initialMatrix(hiddenLayerSizes[0], numInputs);
            for (int i = 1; i < l; i++) {
                weights[i] = initialMatrix(hiddenLayerSizes[i], hiddenLayerSizes[i - 1]);
            }
            weights[l] = initialMatrix(numOutputs, hiddenLayerSizes[l - 1]);
        }
        return weights;
    }

    public double[][] setupOffsets() {
        int l = hiddenLayerSizes.length;
        double[][] offsets = new double[l + 1][];
        if (l == 0) {
            offsets[0] = initialColumn(numOutputs);
        } else {
            offsets[0] = initialColumn(hiddenLayerSizes[0]);
            for (int i = 1; i < l; i++) {
                offsets[i] = initialColumn(hiddenLayerSizes[i]);
            }
            offsets[l] = initialColumn(numOutputs);
        }
        return offsets;
    }

    public double[] initialColumn(int rows) {
        double[][] colMat = initialMatrix(rows, 1);
        double[] col = new double[rows];
        for (int i = 0; i < rows; i++) {
            col[i] = colMat[i][0];
        }
        return col;
    }

    public double[][] initialMatrix(int rows, int columns) {
        // can randomize the zero matrix here if desired
        double[][] out = zeroMatrix(rows, columns);
        for (double[] col : out) {
            for (int i = 0; i < col.length; i++) {
                col[i] = initialValue();
            }
        }
        return out;
    }

    public double initialValue() {
        Random rand = new Random();
        return 0.01 * (2 * rand.nextDouble() - 1);
    }

    public double[][] zeroMatrix(int rows, int columns) {
        return new double[rows][columns];
    }
}
