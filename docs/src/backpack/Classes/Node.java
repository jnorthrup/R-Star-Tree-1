package backpack.Classes;

import java.lang.reflect.Type;
import java.util.ArrayList;

public abstract class Node {
    public static int blockID = 0;
    protected int id;
    protected ArrayList<Node> Children;
    protected Node parent;
    protected MBR rectangle;
    protected int level;

    protected abstract void formMBR();

    public abstract Node clone();

    protected void fill() {

    }

    protected static int assignID() {
        return ++blockID;
    }

    public int getId() {
        return id;
    }
}
