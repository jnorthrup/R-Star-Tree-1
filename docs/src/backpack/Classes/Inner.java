package backpack.Classes;

import java.util.ArrayList;

public class Inner extends Node {

    public Inner(Node parent) {
        this.parent = parent;
    }

    public Inner() {}

    @Override
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

    @Override
    public Node clone() {
        Inner copy = new Inner();
        copy.rectangle = this.rectangle;
        copy.id = this.id;
        return copy;
    }
}
