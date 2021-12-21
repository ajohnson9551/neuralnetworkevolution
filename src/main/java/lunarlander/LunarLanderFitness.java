package lunarlander;

import neuralevolution.Fitness;
import neuralevolution.Network;

public class LunarLanderFitness implements Fitness {

    private final int numGames;
    private final int startMode;

    public LunarLanderFitness(int numGames, int startMode) {
        this.numGames = numGames;
        this.startMode = startMode;
    }

    @Override
    public void score(Network net) {
        double score = 0.0;
        for (int i = 0; i < numGames; i++) {
            LunarLanderGame game = new LunarLanderGame(false, false, false, startMode, i);
            score += game.play(net).computeScore();
        }
        net.setScore((int) (score));
    }

    @Override
    public int getMaxScore() {
        return numGames *(105);
    }
}
