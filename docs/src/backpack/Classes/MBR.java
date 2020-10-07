package backpack.Classes;

import java.util.ArrayList;

//Class describing rectangle objects, used by all descendants of Node Class.

public class MBR {
    Double[][] Edges; //The Edges are stored per dimension in a double[2] array, where [0] contains the minX of the current Dimension and [1] the maxX.
    private double area;
    private double[] center;

    public MBR() {
        Edges = new Double[Main.DIMENSIONS][2];
    }

    //Calculates area of this rectangle.

    public double calculateArea() {
        area = 1;
        for (int i=0; i<Main.DIMENSIONS; i++) {
            area *= Edges[i][1] - Edges[i][0];
        }
        return area;
    }

    //Calculates total overlap area of this and otherRectangle.

    public double calculateOverlap(MBR otherRectangle) {
        double[] dimensionOverlap = new double[Main.DIMENSIONS];
        double total = 1;
        for (int i=0; i<Main.DIMENSIONS; i++) {
            dimensionOverlap[i] = Math.abs(Math.min(this.Edges[i][1], otherRectangle.Edges[i][1]) - Math.max(this.Edges[i][0], otherRectangle.Edges[i][0]));
        }
        for (double PO : dimensionOverlap) {
            total *= PO;
        }
        return total;
    }

    //Calculates totalMargin, i.e. the sum of lengths of all Edges of the rectangle.

    public double calculateMargin() {
        double margin = 0;
        for (int i=0; i<Edges.length; i++) {
            margin += Edges[i][1] - Edges[i][0];
        }
        return margin;
    }

    //Calculates & returns the center of this MBR.

    public double[] getCenter() {
        center = new double[this.Edges.length];
        for (int i=0; i<this.Edges.length; i++) {
            center[i] = this.Edges[i][0] + (this.Edges[i][1] - this.Edges[i][0])/2;
        }
        return center;
    }
}
