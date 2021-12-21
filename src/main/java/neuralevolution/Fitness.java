package neuralevolution;

public interface Fitness {

    void score(Network n);
    int getMaxScore();
}
