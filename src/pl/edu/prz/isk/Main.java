package pl.edu.prz.isk;

import com.softtechdesign.ga.GAException;

import java.util.LinkedList;
import java.util.List;

public class Main {

    private static final int population = 200;
    private static final double crossoverChance = 0.7;
    private static final double randomSelectionChance = 0.05;
    private static final double chromosomeMutationChance = 0.06;
    private static final int maxGenerations = 200;

    private static final String possibleGenes = "ABCDEFGHIJ";
    private static final long[] cutLengths = {15L, 20L, 40L, 29L, 4L, 16L, 60L, 33L, 31L, 15L};
    private static final long logLength = 100L;

    public static void main(String[] args) {
        try
        {
            GACut cut = new GACut(population,
                    crossoverChance,
                    randomSelectionChance,
                    chromosomeMutationChance,
                    maxGenerations,
                    logLength,
                    cutLengths,
                    possibleGenes);
            cut.run();
            String fittestChromosome = cut.getFittestChromosomeString();
            System.out.println(fittestChromosome);
            System.out.println(getCuts(fittestChromosome));
        }
        catch (GAException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static List<Long> getCuts(String chromosome) {
        List<Long> logs = new LinkedList<>();
        Long log = 0L;
        for(char gene : chromosome.toCharArray()) {
            Long cut = cutLengths[possibleGenes.indexOf(gene)];
            if (log + cut > logLength) {
                logs.add(log);
                log = cut;
            } else {
                log += cut;
            }
        }
        logs.add(log);
        return logs;
    }
}
