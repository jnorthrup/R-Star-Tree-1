package backpack.Classes;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class RStarTree implements Serializable {
    private ArrayList<Node> Nodes;
    private Node root;
    private static int M = 5;
    private static int m = 2;
    private int depth;
    private ArrayList<Boolean> Overflown;

    public RStarTree() {
        Nodes = new ArrayList<>();
        root = new Root();
        Nodes.add(root);
        depth = 1;
    }

    public void show() {
        int leafNO = 0;
        int innerNO = 0;
        int node = 0;

        for (int i=0; i<Nodes.size(); i++) {
            if (Nodes.get(i) instanceof Leaf) {
                leafNO++;
            }
            else if (Nodes.get(i) instanceof Inner) {
                innerNO++;
            }
            else {
                node++;
            }
        }
        System.out.println("CURRENT STATE OF R* TREE");
        System.out.println("Total Nodes: " + Nodes.size());
        System.out.println("Leaves: " + leafNO + "\nInner Nodes: " + innerNO + "\nOther: " +node);
    }

    public Node chooseSubTree(Node E,Node N) {
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

    public void overflowTreatment(Node N) {
        Node parent = N.parent;
        if (N.level != 0 && !Overflown.get(N.level)) {
            reinsert(N);
        }
        else {
            split(N);
        }
        Overflown.set(N.level,true);

        if (parent != null) {
            if (parent.Children.size() > M) {
                overflowTreatment(N.parent);
            }
        }
    }

    public void reinsert(Node N) {
        double[] RectDistance = new double[N.Children.size()];
        double[] globalCenter = N.rectangle.getCenter();

        for (int i=0; i<N.Children.size(); i++) {
            RectDistance[i] = euclidean(N.Children.get(i).rectangle.getCenter(),globalCenter);
        }

        double tempDist;
        Node tempChild;
        for (int i=0; i<N.Children.size(); i++) {
            for(int j=1; j < (N.Children.size()-i); j++) {
                if (RectDistance[j-1] < RectDistance[j]) {
                    tempDist = RectDistance[j-1];
                    RectDistance[j-1] = RectDistance[j];
                    RectDistance[j] = tempDist;

                    tempChild = N.Children.get(j-1);
                    N.Children.set(j-1,N.Children.get(j));
                    N.Children.set(j,tempChild);
                }
            }
        }

        ArrayList<Node> Removed = new ArrayList<>();
        int ceiling = M * 30/100;

        for (int i=0; i<ceiling; i++) {
            Removed.set(i,N.Children.remove(i));
        }

        N.formMBR();

        for (int i=0; i<Removed.size(); i++) {
            insert(Removed.get(i));
        }
    }



    public double euclidean(double[] center,double[] globalCenter) {
        double distanceSquared = 0;
        for (int i=0; i<center.length; i++) {
            distanceSquared += Math.pow(center[i] - globalCenter[i],2);
        }
        return Math.sqrt(distanceSquared);
    }

    public void split(Node N) {
        Node[][] OptimalDistro = chooseSplitAxis(N);
        Node leftSub = new Inner();
        Node rightSub = new Inner();
        leftSub.level = 1;
        rightSub.level = 1;

        if (N instanceof Root) {
            Node newRoot = new Root();
            root = newRoot;
            Nodes.add(newRoot);
            depth++;
            leftSub.parent = newRoot;
            rightSub.parent = newRoot;

            for (int i=0; i<Nodes.size(); i++) {
                if (!(Nodes.get(i) instanceof Root)) {
                    Nodes.get(i).level++;
                }
            }
        }
        else {
            leftSub.parent = N.parent;
            rightSub.parent = N.parent;
        }

        for (int i=0; i<OptimalDistro[0].length; i++) {
            OptimalDistro[0][i].parent = leftSub;
            leftSub.Children.add(OptimalDistro[0][i]);
        }

        for (int i=0; i<OptimalDistro[1].length; i++) {
            OptimalDistro[1][i].parent = rightSub;
            rightSub.Children.add(OptimalDistro[1][i]);
        }

        Nodes.remove(N);
        Nodes.add(leftSub);
        Nodes.add(rightSub);
    }

    public Node[][] chooseSplitAxis(Node N) {
        ArrayList<Node[][]> GlobalLDistros = new ArrayList<>();
        ArrayList<Node[][]> GlobalUDistros = new ArrayList<>();
        double minMargin = Double.MAX_VALUE;

        for (int i=0; i<Main.DIMENSIONS; i++) {
            double S = 0;

            Node[] Lower = sortChildren(N.Children,i,0);
            Node[] Upper = sortChildren(N.Children,i,1);
            Node[][] Lgroups = new Node[2][];
            Node[][] Ugroups = new Node[2][];
            ArrayList<Node[][]> LowerDistros = new ArrayList<>();
            ArrayList<Node[][]> UpperDistros = new ArrayList<>();

            for (int k=1; k<=M-2*m+2; k++) {
                Lgroups[0] = new Node[m-1+k];
                Ugroups[0] = new Node[m-1+k];
                Lgroups[1] = new Node[N.Children.size() - (m-1+k)];
                Ugroups[1] = new Node[N.Children.size() - (m-1+k)];

                for (int l = 0; l < m-1+k; l++) {
                    Lgroups[0][l] = Lower[l];
                    Ugroups[0][l] = Upper[l];
                }

                int j = 0;

                for (int l = m-1+k; l<N.Children.size(); l++) {
                    Lgroups[1][j] = Lower[l];
                    Ugroups[1][j] = Upper[l];
                    j++;
                }

                LowerDistros.add(Lgroups);
                UpperDistros.add(Ugroups);

                double currLDistroMargin = distroMargin(Lgroups[0],Lgroups[1]);
                double currUDistroMargin = distroMargin(Ugroups[0],Ugroups[1]);

                S += currLDistroMargin + currUDistroMargin;
            }

            if (S < minMargin) {
                minMargin = S;
                GlobalLDistros = LowerDistros;
                GlobalUDistros = UpperDistros;
            }
        }
        return chooseSplitIndex(GlobalLDistros,GlobalUDistros);
    }

    public Node[][] chooseSplitIndex(ArrayList<Node[][]> LowerDistros,ArrayList<Node[][]> UpperDistros) {
        ArrayList<Node[][]> Finalists = new ArrayList<>();
        double lowerMinOverlap = minOverlapValue(LowerDistros);
        double upperMinOverlap = minOverlapValue(UpperDistros);
        double globalMinOverlap;

        if (lowerMinOverlap < upperMinOverlap) {
            globalMinOverlap = lowerMinOverlap;
        }
        else {
            globalMinOverlap = upperMinOverlap;
        }

        for (int i=0; i<LowerDistros.size(); i++) {
            if (distroOverlap(LowerDistros.get(i)[0],LowerDistros.get(i)[1]) == globalMinOverlap) {
                Finalists.add(LowerDistros.get(i));
            }
            if (distroOverlap(UpperDistros.get(i)[0],UpperDistros.get(i)[1]) < globalMinOverlap) {
                Finalists.add(UpperDistros.get(i));
            }
        }

        if (Finalists.size() == 1) {
            return Finalists.get(0);
        }
        else {
            return minAreaValue(Finalists);
        }

    }

    public Node[][] minAreaValue(ArrayList<Node[][]> Distros) {
        double minArea = Double.MAX_VALUE;
        Node[][] optimalDistro = new Node[2][];

        for (int i=0; i<Distros.size(); i++) {
            if (distroArea(Distros.get(i)[0],Distros.get(i)[1]) < minArea) {
                minArea = distroArea(Distros.get(i)[0],Distros.get(i)[1]);
                optimalDistro = Distros.get(i);
            }
        }
        return optimalDistro;
    }

    public double distroArea(Node[] firstGroup,Node[] secondGroup) {
        Inner firstGroupParent = new Inner();
        Inner secondGroupParent = new Inner();

        firstGroupParent.Children.addAll(Arrays.asList(firstGroup));
        secondGroupParent.Children.addAll(Arrays.asList(secondGroup));

        firstGroupParent.formMBR();
        secondGroupParent.formMBR();

        return firstGroupParent.rectangle.calculateArea() + secondGroupParent.rectangle.calculateArea();
    }

    public double minOverlapValue(ArrayList<Node[][]> Distros) {
        double minOverlapValue = Double.MAX_VALUE;
        double[] OverlapValue = new double[Distros.size()];

        for (int i=0; i<Distros.size(); i++) {
            OverlapValue[i] = distroOverlap(Distros.get(i)[0],Distros.get(i)[1]);

            if (OverlapValue[i] < minOverlapValue) {
                minOverlapValue = OverlapValue[i];
            }
        }
        return minOverlapValue;
    }

    public double distroOverlap(Node[] firstGroup,Node[] secondGroup) {
        Inner firstGroupParent = new Inner();
        Inner secondGroupParent = new Inner();

        firstGroupParent.Children.addAll(Arrays.asList(firstGroup));
        secondGroupParent.Children.addAll(Arrays.asList(secondGroup));

        firstGroupParent.formMBR();
        secondGroupParent.formMBR();

        return firstGroupParent.rectangle.calculateOverlap(secondGroupParent.rectangle);
    }

    public double distroMargin(Node[] firstGroup,Node[] secondGroup) {
        Inner firstGroupParent = new Inner();
        Inner secondGroupParent = new Inner();

        firstGroupParent.Children.addAll(Arrays.asList(firstGroup));
        secondGroupParent.Children.addAll(Arrays.asList(secondGroup));

        firstGroupParent.formMBR();
        secondGroupParent.formMBR();

        return firstGroupParent.rectangle.calculateMargin() + secondGroupParent.rectangle.calculateMargin();
    }

    //Sorts Nodes in ascending order, for the given axis, depending on the current factor (factor = 0 --> Lower / factor = 1 --> Upper).

    public Node[] sortChildren(ArrayList<Node> Nodes,int axis,int factor) {
        Node tempNode;
        for (int i=0; i<Nodes.size(); i++) {
            for(int j=1; j < (Nodes.size()-i); j++) {
                if (Nodes.get(j-1).rectangle.Edges[axis][factor] < Nodes.get(j).rectangle.Edges[axis][factor]) {
                    tempNode = Nodes.get(j-1);
                    Nodes.set(j-1,Nodes.get(j));
                    Nodes.set(j,tempNode);
                }
            }
        }
        Node[] Sorted = new Node[Nodes.size()];
        for (int i=0; i<Nodes.size(); i++) {
            Sorted[i] = Nodes.get(i);
        }
        return Sorted;
    }

    public void insert(Node E) {
        Node N = chooseSubTree(E,root);
        E.parent = N;
        E.level = E.parent.level + 1;
        N.Children.add(E);
        Nodes.add(E);

        if (N.Children.size() > M) {
            Overflown = new ArrayList<>();
            for (int i=0; i<depth; i++) {
                Overflown.add(false);
            }
            overflowTreatment(N);
        }

        while (!(E instanceof Root)){
            E.parent.formMBR();
            E = E.parent;
        }
    }

    public ArrayList<Integer> minOverlapEnlargement(Node E,ArrayList<Node> Nodes) {
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
            if (!N.equals(node)) {
                total += N.rectangle.calculateOverlap(node.rectangle);
            }
        }
        return total;
    }

    public ArrayList<Integer> minAreaEnlargement(Node E, ArrayList<Node> Nodes) {
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

    public void fillLeaf(Leaf E,ArrayList<Entry> Entries) {
        for (Entry entry : Entries) {
            E.getEntries().add(entry);
        }
        E.formMBR();
    }

    public void serialize() throws IOException {
        File out = new File("docs/res/indexfile.bin");
        FileOutputStream fos = new FileOutputStream(out);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
    }

    public void build() {
        try {
            File input = new File("docs/res/datafile.txt");
            Scanner scan = new Scanner(input);

            ArrayList<Entry> Entries = new ArrayList<>();
            int size = 0;

            while (scan.hasNextLine()) {
                String clean = scan.nextLine();
                Entry point = new Entry(clean);
                clean = clean.replaceAll("\\s+", "");
                int sizeOfEntry = clean.getBytes().length;

                if (size + sizeOfEntry <= 32 * 1024) {
                    Entries.add(point);
                    size += sizeOfEntry;
                } else {
                    Leaf E = new Leaf();
                    fillLeaf(E, Entries);
                    insert(E);

                    Entries = new ArrayList<>();
                    Entries.add(point);
                    size = sizeOfEntry;
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("The input file could not be located. The app was terminated.");
        }
    }
}
