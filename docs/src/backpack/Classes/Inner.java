package backpack.Classes;

import java.util.ArrayList;

//Class that describes the Inner Node objects of the R* Tree. They are only created when splits occur and they occupy the space of the tree between its root and its leaves (if the tree reaches a depth > 1 of course).

public class Inner extends Node {

    public Inner() {
        Children = new ArrayList<>();
        rectangle = new MBR();
    }

    @Override
    public Node clone() {
        Inner copy = new Inner();
        copy.rectangle = this.rectangle;
        copy.Children = new ArrayList<>(this.Children);
        return copy;
    }
}
