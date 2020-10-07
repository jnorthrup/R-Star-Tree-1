package backpack.Classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

//This class' objects are functioning by filtering the osm input file, and creating the final state of the datafile used to later construct the R* Tree.
//This application has been programmed to accept .osm input files of 2 dimensional spatial data (id / lat / lon). However, the rest of the algorithms are designed for n-dimensions.

public class OSMParser {
    File OSM;
    File datafile;

    //Stores .osm input file.

    public OSMParser(File OSM) {
        this.OSM = OSM;
        datafile = new File("docs/res/datafile.txt");
    }

    //Filters .osm input file and outputs the datafile.txt which stores the data of one Entry (point in space) per line.
    public void parse() {
        try {
            Scanner scan = new Scanner(OSM);
            PrintWriter writer = new PrintWriter(datafile);
            int n = 0;

            while (scan.hasNext()) {
                String check = scan.next();

                if (check.startsWith("id") || check.startsWith("lat") || check.startsWith("lon")) {
                    writer.write(clean(check) + " ");
                    n++;
                }

                if (n == 3 && scan.hasNext()) {
                    writer.write("\n");
                    n = 0;
                }
            }
            writer.close();
            scan.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Requested file could not be found. App terminated.");
            System.exit(0);
        }
    }

    //Gets a String input and cleans it. Used to isolate information from feature declarations of the input .osm file.
    //E.g. input: id="12392494" output: "12392494"

    public static String clean(String input) {
        StringBuilder sanitized = new StringBuilder();
        boolean wrt = false;

        for (int i=0; i<input.length(); i++) {

            char digit = input.charAt(i);
            if (digit == '\"') {
                if (!wrt) {
                    wrt = true;
                    continue;
                }
                else {
                    break;
                }
            }
            if (wrt) {
                sanitized.append(digit);
            }
        }
        return sanitized.toString();
    }
}
