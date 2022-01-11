package digitrecognition;

import lunarlander.LunarLanderFitness;
import lunarlander.LunarLanderGame;
import neuralevolution.*;

import java.util.Scanner;

public class Run {
    public static void main(String[] args) {
        int[][] data;
        int maxScore;

        int cycles = 3000; // must be at least 100

        int popSize = 22;
        int numBabies = 20;
        int bestBreederPairs = 1;
        int okayBreederPairs = 0;
        double okayThreshold = 0.6; // above this percentile, genomes are considered "okay"
        int cull = 0;
        int randomBreederPairs = 0;
        boolean elitism = true; // if true, culls and replaces bottom, else culls and replaces randomly

        double mutationRate = 1.0;
        int maxMutations = 100;

        NetworkFactory nf = new NetworkFactory(
                new LayeredNetworkParameters(new int[]{20}, 28 * 28, 10),
                LayeredNetwork.class);
        Fitness fit = new DigitRecognitionFitness(true, 0.01);
        maxScore = fit.getMaxScore();
        Population pop = new Population();
        pop.setDependencies(nf, fit);
        pop.setPopulationParams(popSize, numBabies, bestBreederPairs, okayBreederPairs,
                cull, randomBreederPairs, elitism, okayThreshold);
        pop.setGeneticsParams(mutationRate, maxMutations);
        pop.genPop();
        data = pop.evolve(cycles, true);
        DataLineChart chart = new DataLineChart(
                "Digit Recognizer Evolution Data",
                "Score",
                data,
                0,
                maxScore,
                100,
                cycles);
        chart.pack();
        chart.setVisible(true);
        Network champ = pop.getAllTimeChampion();
        champ.print();
        System.out.println("Max score = " + pop.getAllTimeMaxScore());
        Fitness tester = new DigitRecognitionFitness(false, 1.0);
        tester.score(champ);
        double score = champ.getScore();
        double maxTestScore = tester.getMaxScore();
        double percent = ((double) ((int) (1000.0 * score / maxTestScore))) / 10.0;
        System.out.println("Champion Score on Test Data = " + champ.getScore() + "/" + tester.getMaxScore()
                + " = " + percent + "%");
    }
}
