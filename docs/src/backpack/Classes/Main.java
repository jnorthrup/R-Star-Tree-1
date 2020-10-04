package backpack.Classes;

import java.io.File;

public class Main {
    public static int DIMENSIONS;

    public static void main(String[] args) {
        OSMParser Parser = new OSMParser(new File("docs/res/mapclean.osm"));
        Parser.parse();

        //Defining dimensions for input data (as n-dimensional space input sets are accepted)

        DIMENSIONS = 2;
    }
}
