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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DNSTable {

    public static void main(String[] args) {

        // Initialize command parameters
        String fileName = null;
        String dataStructure = null;

        // Setting the options
        Options options = new Options();
        options.addOption("v", false, "verbosity");
        options.addOption("n", true, "filename");
        options.addOption("d", true, "data structure");

        // Parsing command line
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);

            // Checking for filename
            if (cmd.hasOption('n')) {
                fileName = cmd.getOptionValue('n');
            } else {
                throw new InvalidParameterException("Filename wasn't specified");
            }

            // Checking for data structure
            if (cmd.hasOption('d')) {
                dataStructure = cmd.getOptionValue('d');
            } else {
                throw new InvalidParameterException("Data strucure wasn't specified");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Confirming file to process...
        System.out.printf("Processing %s\n", fileName);

        // Create symbol table
        SymbolTable<String> st = SymbolTableFactory.createSymbolTable(dataStructure);

        // Symbol table cannot be null
        if (st == null) {
            throw new NullPointerException("Invalid symbol table type");
        }

        // list with all urls to store in the table
        List<String> urls = new LinkedList<>();

        // Initialize buffered reader to read file.
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(new File(fileName)));

            // For each line...
            String line;
            int counter = 0;
            while ((line = br.readLine()) != null) {

                // DMOZ first column is the url
                String sUrl = line.toLowerCase().split(",")[0];
                urls.add(sUrl);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Symbol table putting began...");

        // for the purpose of computing symbol table performance, stopwatch starts here.
        long start = System.currentTimeMillis(); // start stopwatch

        // Insert urls into table
        urls.forEach(url -> st.put(url, "999.999.999.999"));

        // Store end time stamp
        long end = System.currentTimeMillis();

        // Print results to be compared
        System.out.printf("Total processing time1: %,dms\n", (end - start));
        System.out.printf("\nNumber of urls: %,d\n", st.size());

        // Finding underlying data structure size
        MemoryMeter mm = new MemoryMeter();
        System.out.printf("Symbol table size: %.2f MB\n", (double) mm.measureDeep(st) / 1000000d);
    }
}
