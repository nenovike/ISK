package pl.edu.prz.isk;

import com.softtechdesign.ga.GAException;
import javafx.util.Pair;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static final int population = 200;
    private static final double crossoverChance = 0.75;
    private static final double randomSelectionChance = 0.1;
    private static final double chromosomeMutationChance = 0.1;
    private static final int maxGenerations = 2000;

    private String possibleGenes;
    private long[] cutLengths;
    private long logLength;

    String fittestChromosome;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        prepareData();
        runAlgorithm();
        outputData();
    }

    private void prepareData() {
        BufferedReader reader;
        int numOfCuts;
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku wejsciowego!");
            return;
        }
        try {
            logLength = Long.decode(reader.readLine());
            numOfCuts = Integer.decode(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            System.out.println("Plik wejsciowy ma niewlasciwy format!");
            return;
        }
        if (numOfCuts > 255 || numOfCuts < 1) {
            System.out.println("Niepoprawna liczba belek!");
            return;
        }
        try {
            cutLengths = new long[numOfCuts];
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < numOfCuts; i++) {
                cutLengths[i] = Long.decode(reader.readLine());
                if (cutLengths[i] > logLength) {
                    System.out.println("Niepoprawna dlugosc ciecia!");
                    return;
                }
                builder.append((char) i);
            }
            possibleGenes = builder.toString();
        } catch (IOException | NullPointerException e) {
            System.out.println("Niepoprawna wartosc dlugosci ciecia!");
        } catch (NumberFormatException e) {
            System.out.println("Plik wejsciowy ma niewlasciwy format!");
        }
    }

    private void runAlgorithm() {
        try {
            GACut cut = new GACut(population,
                    crossoverChance,
                    randomSelectionChance,
                    chromosomeMutationChance,
                    maxGenerations,
                    logLength,
                    cutLengths,
                    possibleGenes);
            cut.run();
            fittestChromosome = cut.getFittestChromosomeString();
        } catch (GAException e) {
            System.out.println(e.getMessage());
        }
    }

    private void outputData() {
        try (PrintWriter writer = new PrintWriter("output.txt")) {
            Pair<List<List<Long>>, Long> cutsAndCutouts = getCuts(fittestChromosome);
            List<List<Long>> cuts = cutsAndCutouts.getKey();
            Long cutouts = cutsAndCutouts.getValue();
            cuts.forEach(x -> writer.println(x + " -> " + x.stream().reduce(Long::sum).orElse(0L)));
            writer.println(cuts.size() + "; " + cutouts);
            writer.println("100% -> " + cuts.stream().map(x->x.stream().reduce(Long::sum).orElse(0L)).filter(x->x==logLength).count());
        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku wyjsciowego!");
        }
    }


    private Pair<List<List<Long>>, Long> getCuts(String chromosome) {
        List<List<Long>> logs = new LinkedList<>();
        List<Long> curLog = new LinkedList<>();
        long log = 0L;
        long cutouts = 0L;
        for (char gene : chromosome.toCharArray()) {
            long cut = cutLengths[possibleGenes.indexOf(gene)];
            if (log + cut > logLength) {
                logs.add(curLog);
                cutouts += logLength - log;
                curLog = new LinkedList<>();
                log = 0L;
            }
            log += cut;
            curLog.add(cut);
        }
        logs.add(curLog);
        return new Pair<>(logs, cutouts);
    }
}
