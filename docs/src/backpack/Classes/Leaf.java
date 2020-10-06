package backpack.Classes;

import java.util.ArrayList;

public class Leaf extends Node {
    private static int blockNO = 1;
    private int blockID;
    private ArrayList<Entry> Entries;

    public ArrayList<Entry> getEntries() {
        return Entries;
    }

    public Leaf() {
        Entries = new ArrayList<>();
        Children = null;
        blockID = blockNO++;
        rectangle = new MBR();
    }

    @Override
    protected void formMBR() {
        for (int i=0; i<Main.DIMENSIONS; i++) {
            double min = Entries.get(0).getCoordinates()[i];
            double max = Entries.get(0).getCoordinates()[i];
            for (int j=1; j<Entries.size(); j++) {
                if (Entries.get(j).getCoordinates()[i] < min) {
                    min = Entries.get(j).getCoordinates()[i];
                }
                if (Entries.get(j).getCoordinates()[i] < max) {
                    max = Entries.get(j).getCoordinates()[i];
                }
            }
            rectangle.Edges[i][0] = min;
            rectangle.Edges[i][1] = max;
        }
    }

    @Override
    public Node clone() {
        Leaf copy = new Leaf();
        copy.rectangle = this.rectangle;
        return copy;
    }
}
