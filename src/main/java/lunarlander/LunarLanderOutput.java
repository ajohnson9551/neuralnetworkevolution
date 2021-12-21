package lunarlander;

import neuralevolution.Output;

public class LunarLanderOutput extends Output {

    private boolean thrust;
    private int turn;

    public boolean getThrust() {
        return thrust;
    }

    public int getTurn() {
        return turn;
    }

    @Override
    public void assignFromArray(double[] outArray) {
        switch (outArray.length) {
            case 2 -> {
                this.thrust = outArray[0] > 0;
                this.turn = (int) Math.signum(outArray[1]);
            }
            case 4 -> {
                this.thrust = outArray[3] > 0;
                this.turn = maxIndex(new double[]{outArray[0], outArray[1], outArray[2]}) - 1;
            }
            case 6 -> {
                int outputCode = maxIndex(outArray);
                this.thrust = (outputCode % 2) == 0;
                this.turn = (outputCode % 3) - 1;
            }
        }
    }
}
