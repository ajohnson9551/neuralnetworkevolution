package neuralevolution;

import java.util.ArrayList;
import java.util.Random;

public abstract class Network {

    private int score;

    public abstract Output evaluate(Input in);
    public abstract double[] evaluate(double[] x);
    public abstract void averageWith(Network net2);
    public abstract void mutate();
    public abstract void randomize();
    public abstract void print();

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

}