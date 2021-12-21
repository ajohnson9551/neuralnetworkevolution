package neuralevolution;

import java.util.Random;

public abstract class BasicNetwork extends Network {

    private double[][] weights;
    private double[] offset;

    private final BasicNetworkParameters params;

    public BasicNetwork(BasicNetworkParameters params) {
        this.params = params;
        this.weights = params.setupWeights();
        this.offset = params.setupOffset();
        // surprisingly, it works MUCH better if it starts with all 0s!
//        this.randomize();
    }

    public BasicNetwork(BasicNetwork net) {
        this.params = net.params;
        this.weights = params.setupWeights();
        this.offset = params.setupOffset();
        for (int i = 0; i < params.numOutputs; i++) {
            for (int j = 0; j < params.numInputs; j++) {
                this.weights[i][j] = net.weights[i][j];
            }
           this.offset[i] = net.offset[i];
        }
    }

    @Override
    public void mutate() {
        Random rand = new Random();
        int r = rand.nextInt(params.numOutputs*(1+params.numInputs));
        if (r < params.numOutputs) {
            this.offset[r] = randVal();
        } else {
            r = r - params.numOutputs;
            this.weights[r / params.numInputs][r % params.numInputs] = randVal();
        }
    }

    @Override
    public void randomize() {
        for (int i = 0; i < params.numOutputs; i++) {
            for (int j = 0; j < params.numInputs; j++) {
                this.weights[i][j] = randVal();
            }
            this.offset[i] = randVal();
        }
    }

    @Override
    public void averageWith(Network net2) {
        this.averageWith((BasicNetwork) net2);
    }

    public void averageWith(BasicNetwork net2) {
        Random rand = new Random();
        for (int i = 0; i < params.numOutputs; i++) {
            for (int j = 0; j < params.numInputs; j++) {
                int r = rand.nextInt(3);
                switch (r) {
                    case 0 -> this.weights[i][j] = net2.weights[i][j];
                    case 1 -> this.weights[i][j] = this.weights[i][j];
                    case 2 -> this.weights[i][j] = (this.weights[i][j] + net2.weights[i][j]) / 2.0;
                }
            }
            int r = rand.nextInt(3);
            switch (r) {
                case 0 -> this.offset[i] = net2.offset[i];
                case 1 -> this.offset[i] = this.offset[i];
                case 2 -> this.offset[i] = (this.offset[i] + net2.offset[i]) / 2.0;
            }
        }
    }

    public double randVal() {
        Random rand = new Random();
        return 2.0*rand.nextDouble() - 1.0;
    }

    @Override
    public double[] evaluate(double[] x) {
        double[] y = new double[params.numOutputs];
        for (int i = 0; i < params.numOutputs; i++) {
            for (int j = 0; j < params.numInputs; j++) {
                y[i] += weights[i][j]*x[j];
            }
            y[i] += offset[i];
        }
        return y;
    }

    @Override
    public void print() {
        for (int i = 0; i < params.numOutputs; i++) {
            System.out.print("[ ");
            for (int j = 0; j < params.numInputs; j++) {
                System.out.print(round(weights[i][j]));
                System.out.print(" ");
            }
            System.out.print(" ] ");
            System.out.print("  [ " + round(offset[i]) + " ]");
            System.out.println();
        }
    }

    public String round(double x) {
        String r = x >= 0 ? " " : "";
        r += String.format("%.6f", x);
        return r;
    }
}
