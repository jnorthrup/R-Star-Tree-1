package backpack.Classes;

import java.util.ArrayList;

public class Root extends Node{

    public Root() {
        parent = null;
        level = 0;
        Children = new ArrayList<>();
        rectangle = new MBR();
    }

    @Override
    public Node clone() {
        Root copy = new Root();
        copy.rectangle = this.rectangle;
        return copy;
    }
}
