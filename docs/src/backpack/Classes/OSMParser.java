package backpack.Classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class OSMParser {
    File OSM;
    File datafile;

    public OSMParser(File OSM) {
        this.OSM = OSM;
        datafile = new File("docs/res/datafile.txt");
    }

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
