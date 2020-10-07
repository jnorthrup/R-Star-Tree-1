package backpack.Classes;

import java.lang.reflect.Type;
import java.util.ArrayList;

//Template abstract class for all Nodes (Root,Inner & Leaf objects).

public abstract class Node {
    protected ArrayList<Node> Children;
    protected Node parent;
    protected MBR rectangle;
    protected int level; //Depth of node (distance, in number of nodes, from root)

    //Method responsible for creating the Minimum Bounding Rectangle of the current Node in accordance to their given children Nodes.
    //The Node class implementation is adopted by Root & Inner objects. The Leaf objects override this method, since they form their MBR depending on their point entries, rather than their Children's MBRs (they have no Children).

    protected void formMBR() {
        rectangle = new MBR();
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

    //Abstract method for creating copies of Nodes of any child class.

    public abstract Node clone();
}
