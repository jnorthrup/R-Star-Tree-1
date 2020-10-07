package backpack.Classes;

import java.util.ArrayList;

//Class containing Root Node objects. They describe the root of the R* Tree and their quirks are that they got no parent and always maintain a level of 0.

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
