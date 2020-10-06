package backpack.Classes;

import java.lang.reflect.Type;
import java.util.ArrayList;

public abstract class Node {
    protected ArrayList<Node> Children;
    protected Node parent;
    protected MBR rectangle;
    protected int level;

    protected void formMBR() {
        for (int i=0; i<Main.DIMENSIONS; i++) {
            double min = Children.get(0).rectangle.Edges[i][0];
            double max = Children.get(0).rectangle.Edges[i][1];
            for (int j=1; j<Children.size(); j++) {
                if (Children.get(j).rectangle.Edges[i][0] < min) {
                    min = Children.get(j).rectangle.Edges[i][0];
                }
                if (Children.get(j).rectangle.Edges[i][1] < max) {
                    max = Children.get(j).rectangle.Edges[i][1];
                }
            }
            rectangle.Edges[i][0] = min;
            rectangle.Edges[i][1] = max;
        }
    }

    public abstract Node clone();
}
