package backpack.Classes;

import java.util.ArrayList;

public class Root extends Node{

    public Root() {
        id = assignID();
        parent = null;
        level = 0;
    }

    @Override
    protected void formMBR() {

    }

    @Override
    public Node clone() {
        Root copy = new Root();
        copy.rectangle = this.rectangle;
        copy.id = this.id;
        return copy;
    }
}
