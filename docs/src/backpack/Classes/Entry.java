package backpack.Classes;

import java.util.ArrayList;
import java.util.Scanner;

public class Entry {
    private long id;
    private Double[] Coordinates;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double[] getCoordinates() {
        return Coordinates;
    }

    public void setCoordinates(Double[] coordinates) {
        Coordinates = coordinates;
    }

    //Creating new Entry by collecting the point's dimensions from a line of the datafile and storing them in the Entry's Coordinates Array.

    public Entry(String line) {
        Coordinates = new Double[Main.DIMENSIONS];
        Scanner scan = new Scanner(line);

        if (!line.isEmpty()) {
            id = scan.nextLong();
            int index = 0;
            while (scan.hasNext()) {
                Coordinates[index] = (scan.nextDouble());
                index++;
            }
        }
    }

}
