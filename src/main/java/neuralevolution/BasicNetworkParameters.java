package neuralevolution;

public class BasicNetworkParameters implements NetworkParameters {

    protected final int numInputs;
    protected final int numOutputs;

    public BasicNetworkParameters(Integer numInputs, Integer numOutputs) {
        this.numInputs = numInputs;
        this.numOutputs = numOutputs;
    }

    public double[][] setupWeights() {
        return new double[numOutputs][numInputs];

    }

    public double[] setupOffset() {
        return new double[numOutputs];
    }
}
