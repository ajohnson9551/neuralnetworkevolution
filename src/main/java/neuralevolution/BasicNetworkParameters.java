package neuralevolution;

public class BasicNetworkParameters extends NetworkParameters {

    public BasicNetworkParameters(Integer numInputs, Integer numOutputs) {
        super(numInputs, numOutputs);
    }

    public double[][] setupWeights() {
        return new double[numOutputs][numInputs];

    }

    public double[] setupOffset() {
        return new double[numOutputs];
    }
}
