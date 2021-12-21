package lunarlander;

import neuralevolution.Input;

public class LunarLanderInput extends Input {

    private double alt;
    private double velX;
    private double velY;
    private double ang;

    public LunarLanderInput(double alt, double velX, double velY, double ang) {
        this.alt = alt;
        this.velX = velX;
        this.velY = velY;
        this.ang = ang;
    }

    @Override
    public double[] toArray() {
        return new double[]{this.alt, this.ang, this.velX, this.velY};
    }
}
