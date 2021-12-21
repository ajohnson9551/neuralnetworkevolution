package lunarlander;

public class LunarLanderScore {

    private double remainingFuel;
    private double finalSpeed;

    public LunarLanderScore(double remainingFuel, double finalSpeed) {
        this.remainingFuel = remainingFuel;
        this.finalSpeed = finalSpeed;
    }

    public double computeScore() {
        return ((100.0 - finalSpeed) + 0.05*remainingFuel);
    }
}
