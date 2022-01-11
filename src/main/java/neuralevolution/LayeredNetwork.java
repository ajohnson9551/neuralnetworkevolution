package neuralevolution;

import java.util.Random;

public class LayeredNetwork extends Network {

    private LayeredNetworkParameters params;
    private double[][][] weights;
    private double[][] offsets;


    public LayeredNetwork(LayeredNetworkParameters params) {
        this.params = params;
        this.weights = params.setupWeights();
        this.offsets = params.setupOffsets();
    }

    public LayeredNetwork(LayeredNetwork net) {
        this.params = net.params;
        this.weights = params.setupWeights();
        this.offsets = params.setupOffsets();
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                offsets[i][j] = net.offsets[i][j];
                for (int k = 0; k < weights[i][j].length; k++) {
                    weights[i][j][k] = net.weights[i][j][k];
                }
            }
        }
    }

    @Override
    public double[] evaluate(double[] x) {
        double[] result;
        result = Utility.getUtility().evaluate(weights[0], offsets[0], x, true);
        for (int i = 1; i < weights.length; i++) {
            result = Utility.getUtility().evaluate(weights[i], offsets[i], result, true);
        }
        return result;
    }

    @Override
    public void averageWith(Network net2) {
        this.averageWith((LayeredNetwork) net2);
    }


    public void averageWith(LayeredNetwork net2) {
        Random rand = new Random();
        for (int l = 0; l < weights.length; l++) {
            for (int i = 0; i < weights[l].length; i++) {
                for (int j = 0; j < weights[l][i].length; j++) {
                    int r = rand.nextInt(3);
                    switch (r) {
                        case 0 -> this.weights[l][i][j] = net2.weights[l][i][j];
                        case 1 -> this.weights[l][i][j] = this.weights[l][i][j];
                        case 2 -> this.weights[l][i][j] = (this.weights[l][i][j] + net2.weights[l][i][j]) / 2.0;
                    }
                }
                int r = rand.nextInt(3);
                switch (r) {
                    case 0 -> this.offsets[l][i] = net2.offsets[l][i];
                    case 1 -> this.offsets[l][i] = this.offsets[l][i];
                    case 2 -> this.offsets[l][i] = (this.offsets[l][i] + net2.offsets[l][i]) / 2.0;
                }
            }
        }
    }

    @Override
    public void mutate() {
        Random rand = new Random();
        int l = rand.nextInt(weights.length);
        int rows = weights[l].length;
        int cols = weights[l][0].length;
        int r = rand.nextInt(rows*(1+cols));
        if (r < rows) {
            this.offsets[l][r] = Utility.getUtility().tweakValue(this.offsets[l][r], 0.01);
        } else {
            r = r - rows;
            this.weights[l][r / cols][r % cols] =
                    Utility.getUtility().tweakValue(this.weights[l][r / cols][r % cols], 0.01);
        }
    }

    @Override
    public void print() {
        for (int l = 0; l < weights.length; l++) {
            System.out.println("Layer " + l + ":");
            for (int i = 0; i < weights[l].length; i++) {
                System.out.print("[ ");
                for (int j = 0; j < weights[l][i].length; j++) {
                    System.out.print(Utility.getUtility().roundString(weights[l][i][j]));
                    System.out.print(" ");
                }
                System.out.print(" ] ");
                System.out.print("  [ " + Utility.getUtility().roundString(offsets[l][i]) + " ]");
                System.out.println();
            }
        }
    }
}
