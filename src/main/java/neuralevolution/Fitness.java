package neuralevolution;

public interface Fitness {

    void score(Network net);
    int getMaxScore();
}
