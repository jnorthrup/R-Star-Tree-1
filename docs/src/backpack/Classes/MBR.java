package backpack.Classes;

import java.util.ArrayList;

public class MBR {
    Double[][] Edges;
    double area;

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
}
