package neuralevolution;

import lunarlander.LunarLanderOutput;

public abstract class Network {

    private double score;

    public abstract void averageWith(Network net2);
    public abstract void mutate();
    public abstract void print();
    public abstract double[] evaluate(double[] x);

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public Output evaluate(Input in) {
        Output out = new LunarLanderOutput();
        out.assignFromArray(evaluate(in.toArray()));
        return out;
    }
}