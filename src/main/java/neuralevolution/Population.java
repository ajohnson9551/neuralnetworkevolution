package neuralevolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {

    private int popSize;
    private int numBabies;
    private int bestBreederPairs;
    private int randomBreederPairs;
    private int okayBreederPairs;
    private double okayThreshold;
    private double mutationRate;
    private int maxMutations;
    private int cull;
    private boolean elitism;
    private NetworkFactory nf;
    private Fitness fit;
    private List<Network> pop = new ArrayList<>();
    private Network allTimeChampion;
    private boolean configuredDependencies = false;
    private boolean configuredGenetics = false;
    private boolean configuredPopulation = false;
    private int[][] data;
    private double maxEverScore = Double.MIN_VALUE;

    public Population() {

    }

    public void setDependencies(NetworkFactory nf, Fitness fit) {
        this.nf = nf;
        this.fit = fit;
        configuredDependencies = true;
    }

    public void setPopulationParams(int popSize, int numBabies, int bestBreederPairs, int okayBreederPairs,
                                    int cull, int randomBreederPairs, boolean elitism, double okayThreshold) {
        if (popSize < (randomBreederPairs + bestBreederPairs + okayBreederPairs)*(2 + numBabies) + cull) {
            throw new IllegalArgumentException("Population size too small!");
        }
        this.cull = cull;
        this.popSize = popSize;
        this.numBabies = numBabies;
        this.bestBreederPairs = bestBreederPairs;
        this.okayBreederPairs = okayBreederPairs;
        this.randomBreederPairs = randomBreederPairs;
        this.elitism = elitism;
        this.okayThreshold = okayThreshold;
        configuredPopulation = true;
    }

    public void setGeneticsParams(double mutationRate, int maxMutations) {
        this.mutationRate = mutationRate;
        this.maxMutations = maxMutations;
        configuredGenetics = true;
    }

    public void genPop() {
        for (int i = 0; i < popSize; i++) {
            pop.add(nf.createNew());
        }
    }

    public Network getAllTimeChampion() {
        fit.score(allTimeChampion);
        return allTimeChampion;
    }

    public int[][] evolve(int cycles, boolean printInfo) {
        data = new int[5][cycles];
        if(configuredGenetics && configuredPopulation && configuredDependencies) {
            for (int i = 0; i < cycles; i++) {
                cycle(i, printInfo);
                if (printInfo) {
                    System.out.println("Completed cycle " + i + "/" + cycles);
                }
            }
        } else {
            throw new IllegalStateException("Population class not fully configured yet!");
        }
        data[4][0] = (int) maxEverScore;
        return data;
    }

    private void cycle(int cycleNo, boolean printInfo) {
        Random rand = new Random();
        double totalScore = 0;
        double maxScore = Integer.MIN_VALUE;
        for (Network net : pop) {
            fit.score(net);
            double score = net.getScore();
            if (score > maxScore) {
                maxScore = score;
                if (score > maxEverScore) {
                    maxEverScore = score;
                    allTimeChampion = net;
                }
            }
            totalScore += score;
        }
        data[0][cycleNo] = (int) maxScore;
        data[1][cycleNo] = (int) totalScore / popSize;
        pop.sort(new NetworkComparator());
        if (printInfo) {
            System.out.println("Max score = " + (double) ((int) (1000.0 * maxScore)) / 1000.0 + "/" + fit.getMaxScore());
        }
        List<Network> babies = new ArrayList<>();
        for (int i = 0; i < bestBreederPairs *2; i+=2) {
            for (int j = 0; j < numBabies; j++) {
                Network baby = breed(pop.get(popSize - i - 1), pop.get(popSize - i - 2));
                babies.add(baby);
            }
        }
        int okayThreshold = ((int) (((double) popSize)* this.okayThreshold));
        int headRoom = popSize - okayThreshold;
        for (int i = 0; i < okayBreederPairs; i++) {
            int p1 = rand.nextInt(headRoom);
            int p2;
            do {
                p2 = rand.nextInt(headRoom);
            } while (p1 == p2);
            for (int j = 0; j < numBabies; j++) {
                Network baby = breed(pop.get(okayThreshold + p1), pop.get(okayThreshold + p2));
                babies.add(baby);
            }
        }
        for (int i = 0; i < randomBreederPairs; i++) {
            int j = rand.nextInt(popSize);
            int k = rand.nextInt(popSize);
            while (j == k) {
                k = rand.nextInt(popSize);
            }
            for (int r = 0; r < numBabies; r++) {
                Network baby = breed(pop.get(j), pop.get(k));
                babies.add(baby);
            }
        }
        for (int i = 0; i < (bestBreederPairs + randomBreederPairs + okayBreederPairs)* numBabies; i++) {
            int j = elitism ? 0 : rand.nextInt(pop.size());
            pop.remove(j);
        }
        pop.addAll(babies);
        for (int i = 0; i < cull; i++) {
            int j = elitism ? i : rand.nextInt(pop.size());
            pop.set(j, nf.createNew());
        }
    }

    private Network breed(Network net1, Network net2) {
        Random rand = new Random();
        Network baby = nf.createNew(net1, net2);
        if (rand.nextDouble() < mutationRate) {
            int mutations = rand.nextInt(maxMutations) + 1;
            for (int i = 0; i < mutations; i++) {
                baby.mutate();
            }
        }
        return baby;
    }

    public int getAllTimeMaxScore() {
        return (int) maxEverScore;
    }
}
