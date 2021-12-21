package neuralevolution;

public abstract class Output {

    public abstract void assignFromArray(double[] outArray);

    public int maxIndex(double[] y) {
        int i = 0;
        double max = Double.MIN_VALUE;
        for (int j = 0; j < y.length; j++) {
            if (y[j] > max) {
                max = y[j];
                i = j;
            }
        }
        return i;
    }
}
