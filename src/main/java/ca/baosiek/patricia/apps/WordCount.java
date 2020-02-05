package ca.baosiek.patricia.apps;

import ca.baosiek.patricia.symbolTableUtils.SymbolTable;
import ca.baosiek.patricia.symbolTableUtils.SymbolTableFactory;
import org.apache.commons.cli.*;
import org.github.jamm.MemoryMeter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class WordCount {

    public static void main(String[] args) {

        // Initialie command parameters
        String fileName = null;
        String dataStructure = null;
        AtomicBoolean verbosity = new AtomicBoolean(false);

        // Setting the options
        Options options = new Options();
        options.addOption("v", false, "verbosity");
        options.addOption("n", true, "filename");
        options.addOption("d", true, "data structure");

        // Parsing command line
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse( options, args);

            // Checking for filename
            if (cmd.hasOption('n')){
                fileName = cmd.getOptionValue('n');
            } else {
                throw new InvalidParameterException("Filename wasn't specified");
            }

            // Checking for data structure
            if (cmd.hasOption('d')){
                dataStructure = cmd.getOptionValue('d');
            } else {
                throw new InvalidParameterException("Data strucure wasn't specified");
            }

            // Checking for data structure
            verbosity.set(cmd.hasOption('v'));


        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Confirming file to process...
        System.out.printf("Processing %s\n", fileName);

        // Create symbol table
        SymbolTable<Integer> st = SymbolTableFactory.createSymbolTable(dataStructure);

        // Symbol table cannot be null
        if (st == null){
            throw new NullPointerException("Invalid symbol table type");
        }

        // list with all tokens extracted from file
        List<String> tokenList = new ArrayList<>();

        // Initialize buffered reader to read file.
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(fileName)));
            String line;
            while ((line = br.readLine()) != null) {

                // Filter line before tokenization
                String newLine = filterLine(line);

                // Splits line into tokens
                String[] tokens = newLine.split(" ");

                // Add tokens to tokenList
                tokenList.addAll(Arrays.asList(tokens));
            }
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            if (br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // for the purpose of computing symbol table performance, stopwatch starts here.
        final long start = System.currentTimeMillis(); // start stopwatch

        // Information to user
        System.out.println("Symbol table putting began...");

        // Token counter
        AtomicInteger nTokens = new AtomicInteger();

        // As this process may take some time the following output serves
        // to inform the user that this app is running normally.
        if (verbosity.get()) {
            System.out.println(" ".repeat(60) + "    TOKENS\tDISTINCT\tELAPSED\n");
        }

        // Insert tokens into symbol table
        tokenList.forEach(tk -> insertToken(st, tk, nTokens, start, verbosity.get()));

        // Print last row
        if (verbosity.get()) {
            System.out.println("\n");
        }

        // Print results to be compared
        System.out.printf("Symbol table size is: [%,d]\n", st.size());
        System.out.printf("Total processing time: %,d ms\n", (System.currentTimeMillis() - start));

        // Finding underlying data structure size
        MemoryMeter mm = new MemoryMeter();
        System.out.printf("Symbol table size: %.2f MB\n", (double) mm.measureDeep(st) / 1000000d);

    }

    // Helper method just to enhance code understanding
    private static void insertToken(SymbolTable<Integer> st, String tk, AtomicInteger nTokens,
                                    long start, final boolean verbose) {

        Integer value = st.get(tk);

        if (value != null) st.put(tk, ++value);
        else st.put(tk, 1);

        nTokens.getAndIncrement();

        if (nTokens.get() % 10000 == 0 && verbose) {
            System.out.print("*");
        }

        if (nTokens.get() % 600000 == 0 && verbose) {
            long lap = System.currentTimeMillis();
            System.out.printf(" %9d\t%8d\t%,6dms\n", nTokens.get(), st.size(), (lap - start));
        }
    }

    // Helper method just to enhance code understanding
    private static String filterLine(String line) {

        String newLine = line.toLowerCase()
                // Replaces all punctuations with one whitespace.
                .replaceAll("\\p{Punct}", " ")
                // Replaces all number sequences with one whitespace
                .replaceAll("\\p{Digit}+", " ")
                // Replaces sequences of whitespaces with only one
                .replaceAll("(\\s)+", " ");

        return newLine;

    }
}
