package lunarlander;

import neuralevolution.*;

import java.util.Scanner;

public class Run {
    public static void main(String[] args) {
        int[][] data;
        int maxScore;

        int cycles = 200; // must be at least 100

        int numGames = 30; // should be exactly 30 if startMode is 2
        int startMode = 1; // 0 = fixed, 1 = random, 2 = preset range of 30 starts to iterate through

        int popSize = 200;
        int numBabies = 12;
        int bestBreederPairs = 3;
        int okayBreederPairs = 6;
        double okayThreshold = 0.6; // above this percentile, genomes are considered "okay"
        int cull = 5;
        int randomBreederPairs = 1;
        boolean elitism = false; // if true, culls and replaces bottom, else culls and replaces randomly

        double mutationRate = 0.8;
        int maxMutations = 7;


        if (startMode == 2) {
            numGames = 30;
        } else if (startMode == 0) {
            numGames = 1;
        }

//        NetworkFactory nf = new NetworkFactory (new BasicNetworkParameters(4, 4),
//                LunarLanderNetwork.class);
        NetworkFactory nf = new NetworkFactory (new LayeredNetworkParameters(new int[]{}, 4, 4),
                LayeredNetwork.class);
        Fitness fit = new LunarLanderFitness(numGames, startMode);
        maxScore = fit.getMaxScore();
        Population pop = new Population();
        pop.setDependencies(nf, fit);
        pop.setPopulationParams(popSize, numBabies, bestBreederPairs, okayBreederPairs,
                cull, randomBreederPairs, elitism, okayThreshold);
        pop.setGeneticsParams(mutationRate, maxMutations);
        pop.genPop();
        data = pop.evolve(cycles, true);
        DataLineChart chart = new DataLineChart(
                "Lunar Lander Evolution Data",
                "Score",
                data,
                -maxScore,
                maxScore,
                100,
                cycles);
        chart.pack();
        chart.setVisible(true);
        Scanner scan = new Scanner(System.in);
        Network champ = pop.getAllTimeChampion();
        champ.print();
        System.out.println("Max score = " + pop.getAllTimeMaxScore());
        System.out.println("View champion (score = " + pop.getAllTimeChampion().getScore() + ") piloting the lunar lander? y/n");
        if (scan.nextLine().equalsIgnoreCase("y")) {
            for (int i = 0; i < numGames; i++) {
                LunarLanderGame game = new LunarLanderGame(true, true, false, startMode, i);
                game.play(champ);
            }
        }
    }
}
