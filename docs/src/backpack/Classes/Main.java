package backpack.Classes;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static int DIMENSIONS;

    public static void main(String[] args) throws FileNotFoundException {
        OSMParser Parser = new OSMParser(new File("docs/res/mapclean.osm"));
        Parser.parse();

        //Defining dimensions for input data (as n-dimensional space input sets are accepted)

        DIMENSIONS = 2;

        RStarTree RST = new RStarTree();
        RST.build();
        RST.show();


    }
}
