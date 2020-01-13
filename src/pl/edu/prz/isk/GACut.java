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
        double bonus = 0.0;
        double cutouts  = 0.0;
        long currentLogLength = logLength;
        long usedLogs = 1;
        for (int gene : genes) {
            long cut = cutLengths[this.possGeneValues.indexOf(gene)];
            if (currentLogLength < cut) {
                if (currentLogLength == 0)
                    bonus += 20.0;
                cutouts += Math.log(1 + currentLogLength);
                usedLogs++;
                currentLogLength = logLength;
            }
            currentLogLength -= cut;
        }
        if (currentLogLength == logLength) usedLogs--;
        bonus += cutouts==0.0?Double.POSITIVE_INFINITY:0.0;
        cutouts += currentLogLength;
        return (100.0 - 100.0 * usedLogs / cutLengths.length)
                - (cutouts / Math.log(1 + logLength) / cutLengths.length)
                + bonus;
    }

    public String getFittestChromosomeString() {
        return ((ChromChars) this.getFittestChromosome()).getGenesAsStr();
    }
}
