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

    public Node chooseSubTree(Leaf E,Node N) {
        if (N instanceof Root && (N.Children.isEmpty() || N.Children.get(0) instanceof Leaf)) {
            return N;
        }
        else {

            //If the child pointers in N point to Leaf Nodes:

            if (N.Children.get(0).Children.get(0) instanceof Leaf) {

                //Track & return the Node with minOverlapEnlargement after the insertion of Entry E.

                ArrayList<Integer> Index = minOverlapEnlargement(E,N.Children);
                if (Index.size() == 1) {
                    return N.Children.get(Index.get(0));
                }
                else {

                    //If there were more than one, track & return the Node with minAreaEnlargement after the insertion of Entry E.

                    ArrayList<Node> TiedChildren = new ArrayList<>();
                    for (int i=0; i<Index.size(); i++) {
                        TiedChildren.add(N.Children.get(Index.get(i)));
                    }
                    ArrayList<Integer> NewIndex = minAreaEnlargement(E,TiedChildren);

                    if (NewIndex.size() == 1) {
                        return TiedChildren.get(NewIndex.get(0));
                    }
                    else {

                        //If there were many, return the one with minArea.

                        ArrayList<Node> Finalists = new ArrayList<>();
                        for (int i=0; i<NewIndex.size(); i++) {
                            Finalists.add(TiedChildren.get(NewIndex.get(i)));
                        }
                        int index = minArea(Finalists);
                        return Finalists.get(index);
                    }
                }
            }

            //If the child pointers in N point to Inner Nodes:

            else {

                //Track & return the Node with minAreaEnlargement required to fit Entry E.

                ArrayList<Integer> Index = minAreaEnlargement(E,N.Children);
                if (Index.size() == 1) {
                    chooseSubTree(E,N.Children.get(Index.get(0)));
                }
                else {

                    //If there were many, resolve ties by picking the Node whose rectangle has the minimum area.

                    ArrayList<Node> TiedChildren = new ArrayList<>();
                    for (int i=0; i<Index.size(); i++) {
                        TiedChildren.add(N.Children.get(Index.get(i)));
                    }
                    int index = minArea(TiedChildren);
                    chooseSubTree(E,TiedChildren.get(index));
                }
            }
        }
        return N;
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

    public ArrayList<Integer> minOverlapEnlargement(Leaf E,ArrayList<Node> Nodes) {
        Node copy;
        double min = Double.MAX_VALUE;
        double[] OverlapDiff = new double[Nodes.size()];

        // Calculating minOverlapEnlargement after test-inserting Entry E in all candidates.

        for (int i=0; i<Nodes.size(); i++) {
            copy = Nodes.get(i).clone();
            copy.Children.add(E);
            copy.formMBR();

            double prevOverlap = totalOverlap(Nodes.get(i),Nodes);
            double currOverlap = totalOverlap(copy,Nodes);
            OverlapDiff[i] = currOverlap - prevOverlap;

            if (OverlapDiff[i] < min) {
                min = OverlapDiff[i];
            }
        }

        //Collecting Indices of Nodes matching the minOverlapEnlargement.

        ArrayList<Integer> Index = new ArrayList<>();
        for (int i=0; i<Nodes.size(); i++) {
            if (OverlapDiff[i] == min) {
                Index.add(i);
            }
        }

        return Index;
    }

    public double totalOverlap(Node N, ArrayList<Node> Nodes) {
        double total = 0;
        for (Node node : Nodes) {
            if (N.id != node.id) {
                total += N.rectangle.calculateOverlap(node.rectangle);
            }
        }
        return total;
    }

    public ArrayList<Integer> minAreaEnlargement(Leaf E, ArrayList<Node> Nodes) {
        Node copy;
        double min = Double.MAX_VALUE;
        double[] AreaDiff = new double[Nodes.size()];

        // Calculating minAreaEnlargement after test-inserting Entry E in all candidates.

        for (int i=0; i<Nodes.size(); i++) {
            copy = Nodes.get(i).clone();
            copy.Children.add(E);
            copy.formMBR();

            double prevArea = Nodes.get(i).rectangle.calculateArea();
            double currArea = copy.rectangle.calculateArea();
            AreaDiff[i] = currArea - prevArea;

            if (AreaDiff[i] < min) {
                min = AreaDiff[i];
            }
        }

        //Collecting Indices of Nodes matching the minAreaEnlargement.

        ArrayList<Integer> Index = new ArrayList<>();
        for (int i=0; i<Nodes.size(); i++) {
            if (AreaDiff[i] == min) {
                Index.add(i);
            }
        }

        return Index;
    }

    public int minArea(ArrayList<Node> Nodes) {
        double min = Nodes.get(0).rectangle.calculateArea();
        int index = 0;

        for (int i=1; i<Nodes.size(); i++) {
            if (Nodes.get(i).rectangle.calculateArea() < min) {
                min = Nodes.get(i).rectangle.calculateArea();
                index = i;
            }
        }
        return index;
    }

}