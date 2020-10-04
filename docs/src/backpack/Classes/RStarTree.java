package backpack.Classes;

import java.util.ArrayList;

public class RStarTree {
    private ArrayList<Node> Nodes;
    private Root root;
    private static int M = 50;
    private static int m = 20;
    private int depth;

    public RStarTree() {
        root = new Root();
        depth = 0;
    }

    public Node chooseSubTree(Leaf E,Node N) throws CloneNotSupportedException {
        if (N instanceof Root && (N.Children.isEmpty() || N.Children.get(0) instanceof Leaf)) {
            return N;
        }
        else {
            if (N.Children.get(0).Children.get(0) instanceof Leaf) {
                int index = minOverlapEnlargement(N.Children);
                if (index > 0) {
                    return N.Children.get(index);
                }
                else {

                }
            }
        }
        return N;
    }

    public int minOverlapEnlargement(ArrayList<Node> Nodes) throws CloneNotSupportedException {
        Node copy;
        double min = Double.MAX_VALUE;
        int index;

        for (int i=0; i<Nodes.size(); i++) {
            copy = Nodes.get(i).clone();

        }
    }

    public double calculateTotalOverlap(Node N,ArrayList<Node> Nodes) {
        double total = 0;
        for (int i=0; i<Nodes.size(); i++) {
            if (N.id != Nodes.get(i).id) {
                total += N.rectangle.calculateOverlap(Nodes.get(i).rectangle);
            }
        }
    }

    public int minAreaEnlargement(ArrayList<Node> Nodes) {

    }

    public void overflowTreatment() {

    }

    public void reinsert() {

    }

    public void insert(Leaf E) {
        Node destination = chooseSubTree(E,root);
        if (destination.Children.size() < M) {
            E.parent = destination;
            destination.Children.add(E);
        }
        else {
            overflowTreatment();
        }
    }

}
