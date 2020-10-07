package backpack.Classes;

import java.io.File;
import java.io.FileNotFoundException;

//Build Class. Initializes application, orders the building of the RStarTree object.
//It also accepts the dimensions of the input spatial data as an argument in args[0].

public class Main {
    public static int DIMENSIONS;

    public static void main(String[] args) throws FileNotFoundException {
        OSMParser Parser = new OSMParser(new File("docs/res/mapclean.osm"));
        Parser.parse();

        //Defining dimensions for input data (as n-dimensional space input sets are accepted)

        if (args[0] != null) {
            DIMENSIONS = Integer.parseInt(args[0]);
        }
        else {
            DIMENSIONS = 2;
        }

        //Initializing R* Tree construction.

        RStarTree RST = new RStarTree();
        RST.build();
    }
}
