package backpack.Classes;

import java.util.ArrayList;

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
