package pl.edu.prz.isk;

import com.softtechdesign.ga.*;

public class GACut extends GASequenceList {

    private final long[] cutLengths;
    private final long logLength;

    public GACut(int population, double crossoverChance, double randomSelectionChance, double chromosomeMutationChance, int maxGenerations, long logLength, long[] cutLengths, String possibleGenes) throws GAException {
        super(possibleGenes.length(),
                population,
                crossoverChance,
                (int) (randomSelectionChance * 100),
                maxGenerations,
                0,
                20,
                chromosomeMutationChance,
                0,
                possibleGenes,
                Crossover.ctTwoPoint,
                false);
        this.cutLengths = cutLengths;
        this.logLength = logLength;
    }

    protected double getFitness(int iChromeIndex) {
        char[] genes = this.getChromosome(iChromeIndex).getGenes();
        long currentLogLength = logLength;
        long usedLogs = 0;
        for (int gene : genes) {
            long cut = cutLengths[this.possGeneValues.indexOf(gene)];
            if (currentLogLength < cut) {
                usedLogs++;
                currentLogLength = logLength;
            }
            currentLogLength -= cut;
        }
        if (currentLogLength == logLength) usedLogs++;
        return cutLengths.length - ++usedLogs;// * logLength - cutouts;
    }

    public String getFittestChromosomeString() {
        return ((ChromChars) this.getFittestChromosome()).getGenesAsStr();
    }
}
